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
public class ReturnBook implements Serializable {
    @TableId(value = "return_id", type = IdType.AUTO)
    private Integer returnId; // 归还序号：系统自动生成

    private Integer adminId;
    private Integer borrowId;
    private Integer libraryId;
    private Timestamp time;
}
