package com.se.onlinelibrary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.se.onlinelibrary.bean.user.Authority;
import com.se.onlinelibrary.bean.user.User;
import com.se.onlinelibrary.bean.user.UserDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    List<Authority> findAuthorityByUserId(int id);
    boolean saveUserWithAuthority(UserDetail userDetail);
}
