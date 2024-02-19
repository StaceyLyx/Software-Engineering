package com.se.onlinelibrary.service.userServices;

import com.se.onlinelibrary.bean.user.Authority;
import com.se.onlinelibrary.bean.user.User;
import com.se.onlinelibrary.bean.user.UserDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
    用户信息获取：：自定义的UserDetailService，Security使用该实例进行登录的数据查询
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityService authorityService;

//    返回UserDetails类型
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUserName(username);
//        为查询user表得到的User setAuthority()
        if (user != null) {
            UserDetail userDetail = new UserDetail();
            BeanUtils.copyProperties(user,userDetail);
            Set<Authority> authorities = new HashSet<>();
//            查询user_authority表
            List<Authority> authorityList = userService.findAuthorityByUserId(user.getId());
            authorityList.forEach(authority -> authorities.add(new Authority(authority.getId(),"ROLE_" + authority.getAuthority())));
            userDetail.setAuthorities(authorities);
            return userDetail;
        }
        return null;
    }
}
