package com.se.onlinelibrary.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.onlinelibrary.bean.Library;

import com.se.onlinelibrary.mapper.LibraryMapper;
import com.se.onlinelibrary.service.bookServices.LibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LibraryServiceImpl extends ServiceImpl<LibraryMapper, Library> implements LibraryService {
    public Library getLibrary(int libraryId){
        Wrapper<Library> queryWrapper = new QueryWrapper<Library>().eq("id", libraryId);
        return getOne(queryWrapper);
    }

//    public List<Library> findAllLibrary(){
//        QueryWrapper<Library> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().ne(Library::getId, 0);
//        return list(queryWrapper);
//    }
}



