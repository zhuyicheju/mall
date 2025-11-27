package com.example.mall.common.Utils;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtils {
    private static final String SECRET_KEY = "a1b2c3d4e5f6a7b8c9d0e1f2a3b4c5d6";
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;

    public String generateToken(String username){
        log.info("generateToken - 开始: username={}", username);
        
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRATION);
        log.debug("generateToken - 当前时间={}, 过期时间={}", now, expireDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
        
        log.info("generateToken - 结束: 生成Token成功，token长度={}", token.length());
        return token;
    }

    public Claims parseToken(String token){
        log.info("parseToken - 开始: token长度={}", token.length());
        
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        log.debug("parseToken - Token解析成功: subject={}, 过期时间={}", claims.getSubject(), claims.getExpiration());
        log.info("parseToken - 结束: 返回Claims对象");
        return claims;
    }
}