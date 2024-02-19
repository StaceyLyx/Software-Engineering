package com.se.onlinelibrary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.onlinelibrary.bean.book.Copybook;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CopyBookMapper extends BaseMapper<Copybook> {
}
