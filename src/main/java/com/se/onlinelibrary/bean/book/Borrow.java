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
// 记录借阅信息
public class Borrow implements Serializable {
    @TableId(value = "borrow_id", type = IdType.AUTO)
    private Integer borrowId; // 借阅序号：系统自动生成

    private Integer userId; // 借阅人ID
    private Integer copyId; // 借出副本ID
    private Integer adminId; // 操作的管理员ID
    private Integer libraryId;
    private Timestamp borrowDate;
    private Timestamp dateToReturn;
}
