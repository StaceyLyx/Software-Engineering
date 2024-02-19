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
public class Book implements Serializable {
    @TableId(value = "book_id", type = IdType.AUTO)
    private Integer bookId;
    private String title;
    private String authorName;
    private String description;
    private String isbn;
    private String publishTime;
    private String coverUrl;
}
