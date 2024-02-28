package com.carlinker.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {
    private static final String SECRET = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
    public String generateToken(String email){
        Map<String, Objects> claims = new HashMap<>();
        return CreateToken(claims,email);
    }

    public String CreateToken( Map<String, Objects> claims,String email){
        return Jwts.builder()
                .setClaims(claims).setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)

                .compact();

    }
    private Key getSignKey(){
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
