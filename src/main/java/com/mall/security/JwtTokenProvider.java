package com.mall.security;

import com.mall.config.properties.MallProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * ClassName:JwtTokenProvider
 * Package:com.mall.security
 * Description:
 *
 * @Author:lyp
 * @Create:2026/8/29 - 13:17
 * @Version: v1.0
 *
 */
@Component
public class JwtTokenProvider {
    private final MallProperties mallProperties;

    public JwtTokenProvider(MallProperties mallProperties) {
        this.mallProperties = mallProperties;
    }
    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(
                mallProperties
                        .getJwt()
                        .getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(MallUserDetails userDetails){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + mallProperties.getJwt().getExpiration());

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("userId",userDetails.getUserId())
                .claim("role",userDetails.getRole())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getKey())
                .compact();
    }

    public String getUsername(String token){
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token){
        try {
            getClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
