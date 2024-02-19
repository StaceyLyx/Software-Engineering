package com.se.onlinelibrary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.onlinelibrary.bean.user.UserAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserAuthorityMapper extends BaseMapper<UserAuthority> {
}
