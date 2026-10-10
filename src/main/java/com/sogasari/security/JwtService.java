
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
        private final long refreshExpiration;

        public JwtService(
                        @Value("${jwt.secret}") String secret,
                        @Value("${jwt.expiration}") long expiration,
                        @Value("${jwt.refresh-expiration}") long refreshExpiration) {

                this.signingKey = Keys.hmacShaKeyFor(
                                Decoders.BASE64.decode(secret));

                this.expiration = expiration;
                this.refreshExpiration = refreshExpiration;
        }

        // Existing customer access token
        public String generateToken(String phone) {
                return generateToken(phone, "CUSTOMER");
        }

        // Existing customer/admin access token
        public String generateToken(String phone, String role) {

                Date now = new Date();

                return Jwts.builder()
                                .subject(phone)
                                .claim("role", role)
                                .claim("tokenType", "access")
                                .issuedAt(now)
                                .expiration(new Date(now.getTime() + expiration))
                                .signWith(signingKey)
                                .compact();
        }

        // Generate a separate refresh token
        public String generateRefreshToken(String phone) {

                Date now = new Date();

                return Jwts.builder()
                                .subject(phone)
                                .claim("tokenType", "refresh")
                                .issuedAt(now)
                                .expiration(
                                                new Date(now.getTime() + refreshExpiration))
                                .signWith(signingKey)
                                .compact();
        }

        public String extractPhone(String token) {
                return getClaims(token).getSubject();
        }

        public String extractRole(String token) {
                return getClaims(token).get("role", String.class);
        }

        // Validate normal access tokens
        public boolean isValid(String token) {
                try {
                        Claims claims = getClaims(token);

                        return claims.getExpiration().after(new Date())
                                        && "access".equals(
                                                        claims.get("tokenType", String.class));

                } catch (Exception e) {
                        return false;
                }
        }

        // Validate refresh tokens
        public boolean isValidRefreshToken(String token) {
                try {
                        Claims claims = getClaims(token);

                        return claims.getExpiration().after(new Date())
                                        && "refresh".equals(
                                                        claims.get("tokenType", String.class));

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
