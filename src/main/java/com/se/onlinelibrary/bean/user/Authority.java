package com.se.onlinelibrary.bean.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Authority implements Serializable, GrantedAuthority {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String authority;

    public Authority(String authority) {
        this.authority = authority;
    }
}
