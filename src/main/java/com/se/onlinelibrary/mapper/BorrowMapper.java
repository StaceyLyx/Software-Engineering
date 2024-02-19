package com.se.onlinelibrary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.onlinelibrary.bean.book.Borrow;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BorrowMapper extends BaseMapper<Borrow> {
}
