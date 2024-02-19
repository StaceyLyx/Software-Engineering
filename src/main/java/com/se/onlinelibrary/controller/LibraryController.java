package com.se.onlinelibrary.controller;

import com.se.onlinelibrary.bean.Library;
import com.se.onlinelibrary.service.bookServices.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 图书馆操作
@Slf4j
@RestController
public class LibraryController {

    @Autowired
    LibraryService libraryService;

    // 查询所有图书馆信息
    @RequestMapping(value = "libraryAll", method = RequestMethod.POST)
    public ResponseEntity<Object> findAllLibrary(){
        List<Library> libraryList = libraryService.list();
        Map<String, Object> map = new HashMap<>();
        if(libraryList.isEmpty()){
            map.put("result", "get failed");
            return ResponseEntity.status(400).body(map);
        }else{
            map.put("result", libraryList);
            return ResponseEntity.ok().body(map);
        }
    }
}

