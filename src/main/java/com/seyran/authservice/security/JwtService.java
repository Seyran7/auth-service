package com.seyran.authservice.security;

import com.seyran.authservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private Key getSignKey(){
        byte[] keyBytes;
        return  Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public String generateAccessToken(User user){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractEmail(String token){

        return extractAllClaims(token).getSubject();
    }
    public Boolean validateToken(String token, User user){
        String email = extractEmail(token);
        return email.equals(user.getEmail())&&!TokenExpired(token);
    }
    private boolean TokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());

    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token).getBody();
    }
}
