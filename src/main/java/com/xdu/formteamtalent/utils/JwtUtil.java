package com.xdu.formteamtalent.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
@Slf4j
@ConfigurationProperties(prefix = "auth.jwt")
public class JwtUtil {
    private String secret;
    private int expire;

    /**
     * @description: 生成token
     * @author Jiang Zhihang
     * @date 2022/2/4 22:50
     */
    public String createToken(String openId) {
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
     * @description: 根据用户的token获取claims用于校验token
     * @author Jiang Zhihang
     * @date 2022/2/4 22:53
     */
    public Claims getClaimsByToken(String token) {
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
     * @description: 校验token是否过期
     * @author Jiang Zhihang
     * @date 2022/2/4 23:09
     */
    public boolean isTokenExpired(Date expireDate) {
        return expireDate.before(new Date());
    }
}
