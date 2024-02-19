package com.se.onlinelibrary.bean.book;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Reserve implements Serializable {
    @TableId(value = "reserve_id", type = IdType.AUTO)
    private Integer reserveId;
    private Integer userId;
    private Integer copyId;
    private Timestamp time;
    private String status;
}
