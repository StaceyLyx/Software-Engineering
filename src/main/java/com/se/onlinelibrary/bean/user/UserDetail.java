package com.se.onlinelibrary.bean.user;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;
/*
    处理用户校验逻辑：：有四个状态，满足即可
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class UserDetail extends User implements UserDetails, Serializable {
    @JSONField(name = "userAuthorityId")
    protected Integer userAuthorityId;
    protected String currentAuthority;
    @JSONField(name = "id")
    protected Integer id;
    @JSONField(name = "username")
    protected String username;
    @JSONField(name="password")
    protected String password;
    @JSONField(name = "email")
    protected String email;
    @JSONField(name = "status")
    protected Integer status;
//    角色列表
    @JSONField(name = "authorities")
    private Set<Authority> authorities;

    public UserDetail(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
