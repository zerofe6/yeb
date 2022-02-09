package com.fz.server.config.security.component;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * @author Bo.Fan
 * @date 2021/9/10 15:12
 */
@Component
public class JwtTokenUtils {
    private static final  String CLAIM_KEY_USERNAME = "sub";
    private static final  String CLAIM_KEY_CREATED = "created";
    /**
     * 秘钥和失效时间通过配置获取
     */
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据用户名生成token 获取token中用户名
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String ,Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }
    public String getUserNameFromToken(String token){
        String userName;
        try {
            Claims claims = getCliamsFromToken(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            userName = null;
        }
        return  userName;
    }

    /**
     * 判断token是否过期了
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token,UserDetails userDetails){
        String userNmae = getUserNameFromToken(token);
        return userNmae.equals(userDetails.getUsername())&&!isTokenExpired(token);
    }

    /**
     * 判断token是否能被刷新
     * token 过期能被刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = getCliamsFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }
    /**
     * 根据荷载生成JWT Token
     * @param claims
     * @return
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();

    }

    /**
     * 生成token的失效时间
     * @return
     */
    private Date generateExpirationDate(){
        return new Date(System.currentTimeMillis()+expiration*1000);
    }

    /**
     * 获取token中的荷载
     * @param token
     * @return
     */
    private Claims getCliamsFromToken(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  claims;
    }

    /**
     * 判断token是否过期了
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token){
        Date expireDate = getExpireDateFromToken(token);
        return expireDate.before(new Date());
    }

    /**
     * 获取token中的过期时间
     * @param token
     * @return
     */
    private Date getExpireDateFromToken(String token){
       Claims claims = getCliamsFromToken(token);
       return claims.getExpiration();
    }
}
