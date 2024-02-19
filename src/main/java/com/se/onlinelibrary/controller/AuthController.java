package com.se.onlinelibrary.controller;

import com.se.onlinelibrary.controller.request.RegisterRequest;
import com.se.onlinelibrary.service.userServices.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class AuthController {
    @Autowired
    UserService userService;

//    注册用户"用户名验证"
    @PostMapping( "/register/username")
    public ResponseEntity<Object> ifUsernameExist(String username) {
        log.info("访问后端register/username: user信息为" + username);
//        查询用户名是否注册，如注册会抛出BadRequestController，有相应的状态码响应
        userService.ifUsernameExists(username, false);
//        用户名不存在，可以注册
        return new ResponseEntity<>(HttpStatus.OK);//200
    }

//    合法用户注册
    @PostMapping("/register/submit")
    public ResponseEntity<Map<String,Object>> registerSuccess(RegisterRequest registerRequest) {
        log.info("访问后端register/submit: user信息为" + registerRequest.toString());
//        验证用户名，邮箱合法后注册并返回注册成功状态码;  register函数内进行了异常处理，若注册不合法会返回400状态码
        userService.register(registerRequest, false);
        return ResponseEntity.status(HttpStatus.CREATED)//201
                    .body(null);
    }

//    管理员注册验证账号
    @PostMapping("/superAdmin/addAdmin/username")
    public ResponseEntity<Object> ifUsernameExistAdmin(String username) {
        log.info("访问后端register/username: user信息为" + username);
//        查询用户名是否注册，如注册会抛出BadRequestController，有相应的状态码响应
        userService.ifUsernameExists(username, true);
//        用户名不存在，可以注册
        return new ResponseEntity<>(HttpStatus.OK);//200
    }

//    合法管理员注册（超级管理员）
    @PostMapping("/superAdmin/addAdmin")
    public ResponseEntity<Map<String,Object>> adminRegisterSuccess(RegisterRequest registerRequest) {
        log.info("访问后端/superAdmin/register/submit: user信息为" + registerRequest.toString());
//        验证用户名，邮箱合法后注册并返回注册成功状态码;  register函数内进行了异常处理，若注册不合法会返回400状态码
        userService.register(registerRequest, true);
        return ResponseEntity.status(HttpStatus.CREATED)//201
                .body(null);
    }

    // TODO: 2021/4/15 logout
    @GetMapping("/logout")
    public ResponseEntity<Map<String,Object>> logout() {
        return ResponseEntity.status(HttpStatus.OK)//200
                .body(null);
    }
}
