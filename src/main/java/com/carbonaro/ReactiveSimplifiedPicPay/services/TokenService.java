package com.carbonaro.ReactiveSimplifiedPicPay.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenService {

    private static final long ONE_HOUR = 3600000;
    private static final long EXPIRATION_TIME = ONE_HOUR;

    private static final String SCOPES = "scopes";
    private static final String SECRET_KEY = UUID.randomUUID().toString();
    private static final String MICROSERVICE_NAME = "reactive-simplified-picpay";

    private static final String[] USER_SCOPES = {
            "user:read",
            "user:write"
    };
    private static final String[] ADMIN_SCOPES = {
            "user:read",
            "user:write",
            "admin:read",
            "admin:write"
    };

    public String generateUserToken() {

        return Jwts
                .builder()
                .subject(MICROSERVICE_NAME)
                .claim(SCOPES, USER_SCOPES)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateAdminToken() {

        return Jwts
                .builder()
                .subject(MICROSERVICE_NAME)
                .claim(SCOPES, ADMIN_SCOPES)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    private Key getSigningKey() {

        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
