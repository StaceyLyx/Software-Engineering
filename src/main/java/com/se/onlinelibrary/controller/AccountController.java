package com.se.onlinelibrary.controller;

import com.se.onlinelibrary.bean.user.Authority;
import com.se.onlinelibrary.bean.user.UserAuthority;
import com.se.onlinelibrary.bean.user.UserDetail;
import com.se.onlinelibrary.controller.request.ModifyRequest;
import com.se.onlinelibrary.exception.BadRequestException;
import com.se.onlinelibrary.service.userServices.JwtUserDetailsService;
import com.se.onlinelibrary.service.userServices.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class AccountController {

    @Autowired
    UserService userService;
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @GetMapping("/account/getAllUserInfo")
    public ResponseEntity<Map<String, Object>> getAllUserInfo(String username) {
        Map<String, Object> map = new HashMap<>();
        if (username == null) {
            throw new BadCredentialsException("Username is not valid");
        } else {
            UserDetail userDetail = (UserDetail) jwtUserDetailsService.loadUserByUsername(username);
            if (userDetail == null) {
                throw new BadRequestException("User doesn't exist");
            } else {
                userDetail.setPassword("");
                map.put("userDetail", userDetail);

                List<Authority> authorities = userService.findAuthorityByUserId(userDetail.getId());
                map.put("authority", authorities);

                List<UserAuthority> userAuthorities = userService.getUserAuthorities(userDetail, authorities);
                map.put("user_authority",userAuthorities);
                return ResponseEntity.status(200)
                        .body(map);
            }
        }
    }

    @PostMapping("/account/modifyPassword")
    public ResponseEntity<Map<String,Object>> modifyPassword(ModifyRequest modifyRequest) {
        userService.modifyPassword(modifyRequest);
        return ResponseEntity.status(200)
                .body(null);
    }
}
