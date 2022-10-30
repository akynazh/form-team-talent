package com.xdu.formteamtalent.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@Slf4j
public class JwtUtil {
    private static String secret;
    private static int expire;

    @Value("${auth.jwt.secret}")
    public void setSecret(String secret) {
        JwtUtil.secret = secret;
    }

    @Value("${auth.jwt.expire}")
    public void setExpire(int expire) {
        JwtUtil.expire = expire;
    }

    /**
     * 生成token
     */
    public static String createToken(String openId) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000L); // 乘上1000ms
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(openId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 根据用户的token获取claims用于校验token
     */
    public static Claims getClaimsByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("token error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 校验token是否过期
     */
    public static boolean isTokenExpired(Date expireDate) {
        return expireDate.before(new Date());
    }
}