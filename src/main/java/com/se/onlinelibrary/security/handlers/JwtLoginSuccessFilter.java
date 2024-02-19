package com.se.onlinelibrary.security.handlers;

import com.alibaba.fastjson.JSON;
import com.se.onlinelibrary.bean.user.UserAuthority;
import com.se.onlinelibrary.bean.user.UserDetail;
import com.se.onlinelibrary.security.jwt.JWTUtils;
import com.se.onlinelibrary.service.userServices.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtLoginSuccessFilter implements AuthenticationSuccessHandler {

    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    UserService userService;

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        log.info("-->LOGIN SUCCESS--UserDetail:"+userDetail);
        String loginAuthority = request.getParameter("authority");
        log.info(loginAuthority);
//        根据UserDetail生成token
        String token = jwtUtils.getToken(userDetail);
//        查找当前用户+身份的唯一凭证->userAuthorityId并返回给前端
        UserAuthority userAuthority = userService.getUserAuthorityId(userDetail, loginAuthority);
        log.info(String.valueOf(userAuthority));

        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Authorization", token);
        response.setHeader("Access-Control-Expose-Headers","Authorization");

        Map<String,Object> map = new HashMap<>();
        map.put("userDetail", userDetail);
        map.put("userAuthorityId",userAuthority.getId());
        String json = JSON.toJSONString(map);
        PrintWriter pw = response.getWriter();
        pw.print(json);
        pw.flush();
        pw.close();
    }
}
