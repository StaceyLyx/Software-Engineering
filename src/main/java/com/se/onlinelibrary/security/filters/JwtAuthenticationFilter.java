package com.se.onlinelibrary.security.filters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.se.onlinelibrary.bean.user.UserDetail;
import com.se.onlinelibrary.security.jwt.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 匿名用户放行；
 * 已登录用户检验token，token正确
 */

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private RequestMatcher requiresAuthenticationRequestMatcher;

    @Autowired
    JWTUtils jwtUtils;

    public JwtAuthenticationFilter() {
        this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher("Authorization");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // TODO: Implement the filter.

        //header没带token的，直接放过，因为部分url匿名用户也可以访问
        //如果需要不支持匿名用户的请求没带token，这里放过也没问题，因为SecurityContext中没有认证信息，后面会被权限控制模块拦截
        if (!requiresAuthentication(request, response)) {
            log.info("认证Filter--匿名用户:放行");
            filterChain.doFilter(request, response);
            return;
        }
        String tokenHeader = request.getHeader("Authorization");
        //请求头中没有授权信息
        if (StringUtils.isEmpty(tokenHeader)) {
            log.info("认证Filter--Authorization为空");
            return;
        }
//        if (jwtUtils.getTokenInfo(tokenHeader)) {
//            log.info("认证Filter--Authorization过期");
//            return;
//        }
//            验证token
        // TODO: 2021/4/13 如果当前用户已在上下文中,是否不再需要验证token? 
        DecodedJWT decodedJWT = jwtUtils.getTokenInfo(tokenHeader);
        String claim = decodedJWT.getClaim("userDetail").asString();
        JSONObject jsonObject=JSON.parseObject(claim);      //转换成object
        String userDetailStr = jsonObject.getString("userDetail");

        log.info("认证Filter--token中的claim:"+claim);
        UserDetail userDetail = JSON.parseObject(userDetailStr, UserDetail.class);
        log.info("认证Filter--token匹配User:"+userDetail);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
//            放入Security上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    protected boolean requiresAuthentication(HttpServletRequest request,
                                             HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }
}
