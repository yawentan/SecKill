package top.yawentan.springbootseckill.util;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    private static final String SALT = "yawen";

    /**
     * @description 生成token, 通过加密盐和HS256算法。
     * @param userId
     * @return String token
     */
    public static String createToken(Long userId) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SALT)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*1000));
        return jwtBuilder.compact();
    }


    /**
     * 检查token从token中获得userId
     * @param token
     * @return
     */
    public static Integer checkToken(String token){
        try{
            Map<String, Object> parse = (Map<String, Object>) Jwts.parser().setSigningKey(SALT).parse(token).getBody();
            Integer userId = (Integer)parse.get("userId");
            return userId;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}