package com.se.onlinelibrary.service.userServices;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.onlinelibrary.bean.user.Authority;
import com.se.onlinelibrary.bean.user.User;
import com.se.onlinelibrary.bean.user.UserAuthority;
import com.se.onlinelibrary.bean.user.UserDetail;
import com.se.onlinelibrary.controller.request.ModifyRequest;
import com.se.onlinelibrary.controller.request.RegisterRequest;

import java.util.List;

public interface UserService extends IService<User> {
//    注册相关
    void ifUsernameExists(String username, boolean whetherAdmin);
    void register(RegisterRequest registerRequest, boolean whetherAdmin);
//    登录授权相关
    User findUserByUserName(String username);
    List<Authority> findAuthorityByUserId(int id);
//    account相关
    void modifyPassword(ModifyRequest modifyRequest);
//    List<Authority> getUserAuthority(int userId);
    UserAuthority getUserAuthorityId(UserDetail userDetail, String authority);
    List<UserAuthority> getUserAuthorities(UserDetail userDetail, List<Authority> authorities);

}
