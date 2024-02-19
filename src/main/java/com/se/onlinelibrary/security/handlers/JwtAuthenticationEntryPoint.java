package com.se.onlinelibrary.security.handlers;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Map<String,Object> map = new HashMap<>();
        map.put("message","匿名用户无权访问");//"匿名用户无权访问"
        log.info("-->认证失败--匿名用户无权访问");
        String json = JSON.toJSONString(map);
        response.setContentType("application/json;charset:utf-8");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter pw = response.getWriter();
        pw.print(json);
        pw.flush();
        pw.close();
    }
}
