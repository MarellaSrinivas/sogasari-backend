package com.sogasari.service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
public class GenerateSecret {
    public static void main(String[] args) {
        String secret = Encoders.BASE64.encode(
                Jwts.SIG.HS256.key().build().getEncoded()
        );

        System.out.println(secret);
    }
}