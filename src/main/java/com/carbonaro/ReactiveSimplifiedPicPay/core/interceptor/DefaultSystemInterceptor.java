package com.carbonaro.ReactiveSimplifiedPicPay.core.interceptor;

import com.carbonaro.ReactiveSimplifiedPicPay.core.security.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DefaultSystemInterceptor implements WebFilter {

    private final JwtHandler jwtHandler;

    private static final String CUSTOM_TOKEN_HEADER = "X-Custom-Token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(CUSTOM_TOKEN_HEADER))
                .flatMap(this::validateToken)
                .flatMap(auth -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)))
                .switchIfEmpty(chain.filter(exchange));
    }

    private Mono<AbstractAuthenticationToken> validateToken(String token) {
        try {
            var user = jwtHandler.createUserDetails(token);
            AbstractAuthenticationToken authentication = new JwtHandler.JwtToken(token, user);
            return Mono.just(authentication);
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}
