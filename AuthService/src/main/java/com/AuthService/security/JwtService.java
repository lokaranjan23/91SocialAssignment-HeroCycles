package com.AuthService.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.AuthService.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String generateToken(String email, Role role) {

        return JWT.create()
                .withSubject(email)
                .withClaim("role", role.name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(getAlgorithm());
    }

    public String extractUsername(String token) {

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

    public boolean validateToken(String token) {

        try {

            JWT.require(getAlgorithm())
                    .build()
                    .verify(token);

            return true;

        } catch (Exception ex) {

            return false;
        }
    }
}
