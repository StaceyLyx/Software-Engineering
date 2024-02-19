package com.se.onlinelibrary.service.impl.userImpls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.onlinelibrary.bean.user.Authority;
import com.se.onlinelibrary.bean.user.User;
import com.se.onlinelibrary.bean.user.UserAuthority;
import com.se.onlinelibrary.bean.user.UserDetail;
import com.se.onlinelibrary.controller.request.ModifyRequest;
import com.se.onlinelibrary.controller.request.RegisterRequest;
import com.se.onlinelibrary.exception.BadRequestException;
import com.se.onlinelibrary.exception.CurrentUserHasRegistered;
import com.se.onlinelibrary.mapper.UserMapper;
import com.se.onlinelibrary.service.userServices.AuthorityService;
import com.se.onlinelibrary.service.userServices.JwtUserDetailsService;
import com.se.onlinelibrary.service.userServices.UserAuthorityService;
import com.se.onlinelibrary.service.userServices.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserAuthorityService userAuthorityService;
    @Autowired
    AuthorityService authorityService;
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;


    //提示当前账户的权限状态：会返回三种状态码：
    //200：未注册过 403：账号格式不对 202：已注册过，有相应的权限信息提示，！！！密码不用输
    // TODO: 2021/4/23 管理员注册多返回一个状态码400，因为直接由authority=admin可以直接认证,
    public void ifUsernameExists(String username, boolean whetherAdmin) {
        StringBuilder existAuthorities = new StringBuilder();

        UserDetails userDetails = getRegisteredUser(username, whetherAdmin);
        // 当前账户已注册过
        if (userDetails != null) {
            if (whetherAdmin) {
                throw new BadRequestException("Warn:This account '" + username + "' has registered as admin");
            } else {
                for (GrantedAuthority authority : userDetails.getAuthorities()) {
                    //403
                    existAuthorities.append("'").append(authority.getAuthority().substring(5)).append("'");
                }
                //202
                throw new CurrentUserHasRegistered("Hint:This account '" + username + "' has registered as " + existAuthorities
                        + ", please use original password for register");
            }
        }
    }

    //判断当前账户是否被注册过，注册过则返回，未注册返回null
    private UserDetails getRegisteredUser(String username, boolean whetherAdmin) {
        // TODO: 2021/4/13 正则表示：用户名匹配复旦学号
        String USERNAME;
        if (whetherAdmin) {
            USERNAME = "^[-]|^[A-z][A-z0-9_\\-]+";
        } else {
            USERNAME = "^[0-9]{11}$"; //""/^[-]|^[A-z][A-z0-9_\\-]+/
        }
        if (username == null) {
            throw new BadCredentialsException("Username is not legal");
        }
        if (!Pattern.matches(USERNAME,username)) {
            throw new BadCredentialsException("Username '" + username + "' is not legal");
        }
        return jwtUserDetailsService.loadUserByUsername(username);
    }

//    public void ifEmailExists(String email) {
//        String EMAIL = "/^([a-z]|[A-Z]|[0-9])(\\w|\\-)+@[a-zA-Z0-9]+((\\.[a-zA-Z]{2,4}){1,2})$/";
//        if (!Pattern.matches(EMAIL,email))
//            throw new BadCredentialsException("Email '" + email + "' is not legal");
//        Wrapper<User> queryWrapper = new QueryWrapper<User>().eq("email", email);
//        User findUser = getOne(queryWrapper);
//        if (findUser != null)
//            throw new BadCredentialsException("Email '" + email + "' has been registered");
//    }

    @Override
    public User findUserByUserName(String username) {
//        引入lambda，避免在代码中写类似的于"username"的硬编码
        return this.baseMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getUsername,username).ne(User::getStatus,1));
    }

    @Override
    public List<Authority> findAuthorityByUserId(int id) {
        return this.baseMapper.findAuthorityByUserId(id);
    }

    @Override
    public void modifyPassword(ModifyRequest modifyRequest) {
        String username = modifyRequest.getUsername();
        String oldPassword = modifyRequest.getOldPassword();
        String newPassword = modifyRequest.getNewPassword();
        if (username == null || oldPassword == null || newPassword == null)
            throw new BadCredentialsException("ModifyRequest is not legal");
        else if (newPassword.equals(oldPassword)) {
            throw new BadCredentialsException("New password is the same as the old password");
        }
        else {
            User user = findUserByUserName(username);
            if (user == null)
                throw new BadRequestException("User doesn't exist");
            else {
                if (!isPasswordLegal(username,newPassword)) {
                    throw new BadCredentialsException("New password is not legal");
                } else if (!passwordEncoder.matches(oldPassword,user.getPassword())) {
                    throw new BadRequestException("Old password is not correct");
                } else {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    update(user,new QueryWrapper<User>().lambda().eq(User::getUsername,username));
                }
            }
        }
    }

    @Override
    public UserAuthority getUserAuthorityId(UserDetail userDetail, String authority) {
        int userId = userDetail.getId();
        int authorityId = 0;
        for (Authority thisAuthority : userDetail.getAuthorities()) {
            if (thisAuthority.getAuthority().equals("ROLE_" + authority)) {
                authorityId = thisAuthority.getId();
                break;
            }
        }
        if (authorityId != 0) {
            return userAuthorityService.getOne(new QueryWrapper<UserAuthority>().lambda().eq(UserAuthority::getUserId, userId)
                .eq(UserAuthority::getAuthorityId,authorityId));
        }
        return null;
    }

    @Override
    public List<UserAuthority> getUserAuthorities(UserDetail userDetail, List<Authority> authorities) {
        List<UserAuthority> userAuthorities = new ArrayList<>();
        for (Authority authority : authorities) {
            UserAuthority userAuthority = getUserAuthorityId(userDetail, authority.getAuthority());
            if (userAuthority != null) {
                userAuthorities.add(userAuthority);
            }
        }
        return userAuthorities;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    @Override
    public void register(RegisterRequest registerRequest, boolean whetherAdmin) {
        // 正则严谨的验证注册用户名，邮箱，密码，身份是否符合网站要求
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();
        String authority = registerRequest.getAuthority();
        if (username == null || password == null || email == null || authority == null)
            throw new BadCredentialsException("Request id not legal");
        // user_role增加记录-->UserMapper设置sql直接同时操作——使用事务机制
        //当前账户1。当前*身份未注册，2。密码合法，3。邮箱对应账号时
        if (ifUsernameAuthority(registerRequest, whetherAdmin) && isPasswordLegal(username, password) && isEmailMatch(username, email)) {
            //当前帐号注册过其他身份，新增身份
            if (getRegisteredUser(username, whetherAdmin) != null) {
                // TODO: 2021/4/20  验证密码，提示密码需相同
                //User表 for userid
                User hasRegisteredUser = findUserByUserName(username);
                //Authority表 for authorityid
                Authority authorityContainId = authorityService.getOne(new QueryWrapper<Authority>().eq("authority",authority));

                //UserAuthority表 插入身份信息
                UserAuthority userAuthority = new UserAuthority(hasRegisteredUser.getId(),authorityContainId.getId());
                userAuthorityService.save(userAuthority);
            } else { //当前帐号未注册过其他身份，新增用户条目+身份
                password = passwordEncoder.encode(password);
                User user = new User(username, password, email);
                //User表 插入用户信息
                save(user);

                User justSaveUser = findUserByUserName(username);
                Authority authorityContainId = authorityService.getOne(new QueryWrapper<Authority>().eq("authority",authority));

                UserAuthority userAuthority = new UserAuthority(justSaveUser.getId(),authorityContainId.getId());
                userAuthorityService.save(userAuthority);
            }
        }
    }

    //准确判断"当前用户名"和"当前权限"是否被注册过，判断是否是合法的注册用户，合法返回true
    public boolean ifUsernameAuthority(RegisterRequest registerRequest, boolean whetherAdmin) {
        String username = registerRequest.getUsername();
        String authority = registerRequest.getAuthority();
        String USERNAME;
        if (whetherAdmin) {
            USERNAME = "^[-]|^[A-z][A-z0-9_\\-]+";
        } else {
            USERNAME = "^[0-9]{11}$";
        }
        if (!Pattern.matches(USERNAME,username))
            throw new BadCredentialsException("Username '" + username + "' is not legal");
        if (whetherAdmin) {
            if (!(authority.equals("admin"))) {
                throw new BadCredentialsException("Authority '" + authority + "' is not legal");
            }
        } else {
            if (!(authority.equals("student") || authority.equals("teacher") || authority.equals("admin") || authority.equals("superAdmin")))
                throw new BadCredentialsException("Authority '" + authority + "' is not legal");
        }

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
        if (userDetails != null) {
            for (GrantedAuthority existAuthority : userDetails.getAuthorities()) {
                if ((existAuthority.getAuthority().substring(5)).equals(authority)) {
//                    当前身份注册过：返回状态码400
                    throw new BadRequestException("Warn:This account '" + username + "' has registered as '" + authority +"'");
                }
            }
        }
        return true;
    }

    boolean isPasswordLegal(String username, String password) {
        // 字⺟，数字或者特殊字符（-_）
        String REG_NEW = "^[A-z0-9\\-_]{6,32}$";
        // 至少两种==不能只有一种
        // 只有数字若干
        String REG_NUM = "^[0-9]{6,32}$";
        // 只有字母若干
        String REG_CHAR = "^[A-z]{6,32}$";
        // 只有-和_若干
        String REG_SPEC = "^[_\\-]{6,32}$";
        if (password.contains(username))
            throw new BadCredentialsException("Password shouldn't contain username");
        else if (!Pattern.matches(REG_NEW,password))
            throw new BadCredentialsException("Password contains illegal signature or too long or too short");
        else if (Pattern.matches(REG_NUM,password) || Pattern.matches(REG_CHAR,password) || Pattern.matches(REG_SPEC,password))
            throw new BadCredentialsException("Password's security is bad");
        else return true;
    }

    // TODO: 2021/4/14 验证邮箱是否和账号匹配:可能会根据需求进一步更改
    boolean isEmailMatch(String username, String email) {
        if (!email.equals(username+"@fudan.edu.cn")){
            throw new BadCredentialsException("username and email don't match");
        } else return true;
    }
}
