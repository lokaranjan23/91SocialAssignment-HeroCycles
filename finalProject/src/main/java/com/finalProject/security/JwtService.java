package com.finalProject.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public boolean validateToken(String token) {

        try {
            JWT.require(getAlgorithm())
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return JWT.require(getAlgorithm())
                .build()
                .verify(token)
                .getSubject();
    }

    public String extractRole(String token) {
        return JWT.require(getAlgorithm())
                .build()
                .verify(token)
                .getClaim("role")
                .asString();
    }
}