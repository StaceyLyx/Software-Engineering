package com.se.onlinelibrary.security.jwt;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.se.onlinelibrary.bean.user.UserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {
    private static final long serialVersionUID = -3839549913040578986L;

    @Autowired
    private JwtConfigProperties jwtConfigProperties;

    //生成Token（header,payload,sig）
    public String getToken(UserDetail userDetail) {
//        设置过期时间
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(Calendar.MILLISECOND, jwtConfigProperties.getValidity());
//        将claims设置用到的UserDetail转成JSON格式
//        密码设为空，禁止token传递时包括密码
        userDetail.setPassword("");
        Map<String,Object> map = new HashMap<>();
        map.put("userDetail", userDetail);
        String json = JSON.toJSONString(map);

        JWTCreator.Builder builder = JWT.create();
        builder
                .withSubject(userDetail.getUsername())//username
                .withClaim("userDetail", json);//userDetail
        return builder
                .withExpiresAt(expireTime.getTime())//过期时间
                .sign(Algorithm.HMAC256(jwtConfigProperties.getSecret()));//加密
    }

    //验证token合法性，返回token信息
    public DecodedJWT getTokenInfo(String token) {
        return JWT.require(Algorithm.HMAC256(jwtConfigProperties.getSecret())).build().verify(token);
    }

    public String getUsernameFromToken(String jwtToken) {
        return getClaimFromToken(jwtToken, Claims::getSubject);
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String username = getUsernameFromToken(jwtToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    private Date getExpirationDateFromToken(String jwtToken) {
        return getClaimFromToken(jwtToken, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String jwtToken) {
        return Jwts.parser().setSigningKey(jwtConfigProperties.getSecret()).parseClaimsJws(jwtToken).getBody();
    }

    public boolean isTokenExpired(String jwtToken) {
        final Date expiration = getExpirationDateFromToken(jwtToken);
        return expiration.before(new Date());
    }
}
