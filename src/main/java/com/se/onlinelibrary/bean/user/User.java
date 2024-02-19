package com.se.onlinelibrary.bean.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Component
public class User implements Serializable {

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = 0;
    }

    @TableId(value = "id", type = IdType.AUTO)
    protected Integer id;
    protected String username;
    protected String password;
    protected String email;
    protected Integer status;
    // TODO: 2021/4/14 管理员登录地点条目待创建 
}
