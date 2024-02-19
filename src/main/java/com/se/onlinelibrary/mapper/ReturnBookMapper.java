package com.se.onlinelibrary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.onlinelibrary.bean.book.ReturnBook;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ReturnBookMapper extends BaseMapper<ReturnBook> {
}
