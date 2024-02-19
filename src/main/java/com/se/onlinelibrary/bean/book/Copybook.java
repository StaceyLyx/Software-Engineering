package com.se.onlinelibrary.bean.book;

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
public class Copybook implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id; // 副本序号：系统自动生成

    private String isbn;
    private Integer bookId; // 副本ID：对应Book的bookId
    private Integer libraryId;
    private String status;
}
