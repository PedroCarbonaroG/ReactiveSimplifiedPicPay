package com.carbonaro.ReactiveSimplifiedPicPay.core.security;

import static com.carbonaro.ReactiveSimplifiedPicPay.services.TokenService.SECRET_KEY;

import com.carbonaro.ReactiveSimplifiedPicPay.services.exceptions.TransactionValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ValidationException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.result.condition.PatternsRequestCondition;
import org.springframework.web.reactive.result.method.AbstractHandlerMethodMapping;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPattern;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityScopesFilter implements WebFilter {

    private static final String AUTH_HEADER = "AUTH-API-TOKEN";

    private Map<String, HandlerMapping> handlerMappings;

    @Override
    @SneakyThrows
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return Mono.just(exchange)
                .flatMap(this::interceptToValidateToken)
                .flatMap(unused -> chain.filter(exchange))
                .doOnError(errorResponse -> Mono.error(new Exception()))
                .then();
    }

    private Mono<Boolean> interceptToValidateToken(ServerWebExchange exchange) {

        String token = resolveToken(exchange.getRequest().getHeaders().toString());
        if (token == null) return Mono.just(Boolean.TRUE);
        List<String> routeScopes = getScopesFromRoute(exchange);

        return Mono.just(validateToken(token, routeScopes));
    }
    private String resolveToken(String bearerToken) {

        Pattern pattern = Pattern.compile(AUTH_HEADER + ":\"(.*?)\"");
        Matcher matcher = pattern.matcher(bearerToken);
        return matcher.find() ? matcher.group(1) : null;
    }
    private List<String> getScopesFromRoute(ServerWebExchange exchange) {
        List<String> listOfScopes = new ArrayList<>();

        for (HandlerMapping handlerMapping : handlerMappings.values()) {

            if (handlerMapping instanceof AbstractHandlerMethodMapping) {

                AbstractHandlerMethodMapping<RequestMappingInfo> methodMapping = (AbstractHandlerMethodMapping<RequestMappingInfo>) handlerMapping;
                Map<RequestMappingInfo, HandlerMethod> handlerMethods = methodMapping.getHandlerMethods();

                listOfScopes.addAll(getScopesFromHandlerMethods(handlerMethods, exchange));
            }
        }

        return listOfScopes;
    }
    private List<String> getScopesFromHandlerMethods(Map<RequestMappingInfo, HandlerMethod> handlerMethods, ServerWebExchange exchange) {

        List<String> listOfScopes = new ArrayList<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {

            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();

            listOfScopes.addAll(getScopesFromPatternsCondition(patternsCondition, exchange, handlerMethod));
        }

        return listOfScopes;
    }
    private List<String> getScopesFromPatternsCondition(PatternsRequestCondition patternsCondition, ServerWebExchange exchange, HandlerMethod handlerMethod) {

        List<String> listOfScopes = new ArrayList<>();

        for (Object pattern : patternsCondition.getPatterns()) {

            if (pattern instanceof PathPattern pathPattern && pathPattern.matches(exchange.getRequest().getPath().pathWithinApplication())) {
                SecurityScopes annotation = handlerMethod.getMethodAnnotation(SecurityScopes.class);

                if (annotation != null) {

                    listOfScopes.addAll(Arrays.asList(annotation.scopes()));
                }
            }
        }

        return listOfScopes;
    }
    @SneakyThrows
    private boolean validateToken(String token, List<String> routeScopes) {

        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            List<String> tokenScopes = claims.get("scopes", List.class);

            Set<String> tokenScopesSet = new HashSet<>(tokenScopes);
            Set<String> routeScopesSet = new HashSet<>(routeScopes);

            if (tokenScopesSet.containsAll(routeScopesSet)) {
                return true;
            } else {
                throw new TransactionValidationException();
            }

        }
        catch (ExpiredJwtException e) { throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage()); }
        catch (SignatureException e) { throw new SignatureException(e.getMessage()); }
        catch (TransactionValidationException e) { throw new ValidationException(e.getMessage()); }

    }

}
