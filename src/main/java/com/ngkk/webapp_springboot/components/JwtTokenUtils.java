package com.ngkk.webapp_springboot.components;

import com.ngkk.webapp_springboot.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secret_key}")
    private String secretKey;

    public String generateToken(User user) {
        //properties => claims
        Map<String, Object> claims = new HashMap<>();
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e) {
          log.warn("Cannot generate token ,error"+e.getMessage());
            return null;
        }
    }
    private Key getSignKey() {
        byte [] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }
    private Claims extractAllClaims(String token) {
       return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public  <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
    final Claims claims = this.extractAllClaims(token);
    return claimsResolver.apply(claims);
    }
    //check expiration
    public boolean isTokenExpired(String token) {
        Date expiration = this.extractClaim(token,Claims::getExpiration);
        return expiration.before(new Date());
    }
    public String extractPhoneNumber(String token) {
        return extractClaim(token,Claims::getSubject);
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        String phonenumber = extractPhoneNumber(token);
        return phonenumber.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}


