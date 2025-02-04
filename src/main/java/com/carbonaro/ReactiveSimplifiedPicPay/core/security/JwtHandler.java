package com.carbonaro.ReactiveSimplifiedPicPay.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtHandler implements ReactiveAuthenticationManager, ServerAuthenticationConverter {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token-expiration-seconds}")
    private long tokenExpiration;

    private static final String BEARER = "Bearer ";

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        return Mono.just(authentication)
                .cast(JwtToken.class)
                .filter(jwtToken -> isTokenValid(jwtToken.getToken()))
                .map(JwtToken::withAuthenticated)
                .switchIfEmpty(Mono.error(new AuthenticationServiceException("Invalid token.")));
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {

        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(header -> header.startsWith(BEARER))
                .map(header -> header.substring(BEARER.length()))
                .map(token -> new JwtToken(token, createUserDetails(token)));
    }

    private UserDetails createUserDetails(String token) {

        String username = extractUsername(token);
        return User.builder()
                .username(username)
                .authorities(createAuthorities(token))
                .password("")
                .build();
    }

    private List<SimpleGrantedAuthority> createAuthorities(String token) {

        return extractRoles(token).stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    List<String> extractRoles(String jwt) {
        return extractClaim(jwt, claims -> (List<String>) claims.get("roles"));
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    boolean isTokenValid(String jwt) {
        return !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return extractClaim(jwt, Claims::getExpiration).before(new Date());
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        long currentTimeMillis = System.currentTimeMillis();
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(role -> role.substring("ROLE_".length()))
                        .toArray())
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(currentTimeMillis + tokenExpiration * 1000))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private <T> T extractClaim(String jwt, Function<Claims, T> claimResolver) {

        Claims claims = extractAllClaims(jwt);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (JwtException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    private SecretKey getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    @Getter
    private static class JwtToken extends AbstractAuthenticationToken {

        private final String token;
        private final UserDetails principal;

        JwtToken(String token, UserDetails principal) {
            super(principal.getAuthorities());
            this.token = token;
            this.principal = principal;
        }

        Authentication withAuthenticated() {
            JwtToken cloned = new JwtToken(token, principal);
            cloned.setAuthenticated(true);
            return cloned;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof JwtToken test)) {
                return false;
            }
            if (this.getToken() == null && test.getToken() != null) {
                return false;
            }
            if (this.getToken() != null && !this.getToken().equals(test.getToken())) {
                return false;
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            int code = super.hashCode();
            if (this.getToken() != null) {
                code ^= this.getToken().hashCode();
            }
            return code;
        }
    }

}
