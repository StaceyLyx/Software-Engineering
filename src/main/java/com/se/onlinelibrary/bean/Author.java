package com.se.onlinelibrary.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    @TableId(value = "id", type = IdType.AUTO)
    private int authorId;
    private String name;
    private Date birth;
    private String gender;
    private String introduction;
}
