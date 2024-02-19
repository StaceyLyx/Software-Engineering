package com.se.onlinelibrary.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.onlinelibrary.bean.user.UserDetail;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
            Map<String, String> loginData = new HashMap<>();
            try {
                loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
                log.info("1."+loginData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String username = loginData.get(getUsernameParameter());
            String password = loginData.get(getPasswordParameter());
            if (username == null) {
                username = "";
            }
            if (password == null) {
                password = "";
            }
            username = username.trim();
            UserDetail userDetail = new UserDetail(username, password);
//            BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
//            System.out.println(encode.encode(userDetail.getPassword()));
            log.info("登录Filter--前端User："+userDetail);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    username, password);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } else {
//            BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
//            System.out.println(encode.encode(userDetail.getPassword()));
            log.info("登录Filter--前端User：");
            return super.attemptAuthentication(request, response);
        }
    }

//    @Bean
//    JwtLoginFilter jwtLoginFilter() throws Exception {
//        JwtLoginFilter loginFilter = new JwtLoginFilter();
//        loginFilter.setAuthenticationSuccessHandler(new JwtLoginSuccessFilter());
//        loginFilter.setAuthenticationFailureHandler(new JwtLoginFailureHandler());
//        loginFilter.setFilterProcessesUrl("/login");
//        return loginFilter;
//    }

}
