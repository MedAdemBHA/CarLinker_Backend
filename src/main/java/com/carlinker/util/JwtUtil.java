package com.carlinker.util;

import com.carlinker.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET = "84d38876b12c843f4670837210169abafa43de2eebc733957af68ca1d3544e8b";
    private static final long EXPIRATION_TIME_MILLIS = 1000 * 60 * 30; // 30 minutes

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    public String generateToken(String email) {
        return createToken(new HashMap<>(), email);
    }

    public String generateToken(String email, UserRole userRole, String name, long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userRole", userRole);
        claims.put("userId", userId);
        claims.put("name", name);
        return createToken(claims, email);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS));

        // Use the dynamic key for signing
        Key signKey = getSignKey();
        return jwtBuilder.signWith(signKey, SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        // Decode the Base64-encoded dynamic key
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
