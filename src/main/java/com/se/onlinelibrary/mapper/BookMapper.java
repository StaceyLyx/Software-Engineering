package com.se.onlinelibrary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.onlinelibrary.bean.book.Book;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookMapper extends BaseMapper<Book> {
    List<Book> findBookBySearchStringFromIsbn(String searchString);
    List<Book> findBookBySearchStringFromAuthorName(String searchString);
    List<Book> findBookBySearchStringFromTitle(String searchString);
}
