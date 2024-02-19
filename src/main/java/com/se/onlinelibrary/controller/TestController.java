package com.se.onlinelibrary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    @PostMapping("/test/post")
    public ResponseEntity<Map<String,Object>> testPost() {
        Map<String,Object> map = new HashMap<>();
        map.put("msg","Post访问成功");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/test/get")
    public ResponseEntity<Map<String,Object>> testGet() {
        Map<String,Object> map = new HashMap<>();
        map.put("msg","Get访问成功");
        return ResponseEntity.ok(map);
    }


    @GetMapping("/admin")
    public ResponseEntity<Map<String,Object>> authenticationAfterLogin() {
        Map<String,Object> map = new HashMap<>();
        map.put("msg","admin访问成功");
        return ResponseEntity.ok(map);
    }

}
