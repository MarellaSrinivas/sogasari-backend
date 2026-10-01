package com.sogasari.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );

        this.expiration = expiration;
    }

    // Existing customer login
    public String generateToken(String phone) {

        return generateToken(
                phone,
                "CUSTOMER"
        );
    }

    // New method for customer/admin
    public String generateToken(
            String phone,
            String role
    ) {

        Date now = new Date();

        return Jwts.builder()
                .subject(phone)

                .claim(
                        "role",
                        role
                )

                .issuedAt(now)

                .expiration(
                        new Date(
                                now.getTime() + expiration
                        )
                )

                .signWith(signingKey)
                .compact();
    }

    public String extractPhone(String token) {

        return getClaims(token)
                .getSubject();
    }

    public String extractRole(String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    public boolean isValid(String token) {

        try {

            Claims claims = getClaims(token);

            return claims.getExpiration()
                    .after(new Date());

        } catch (Exception e) {

            return false;
        }
    }

    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}