package com.se.onlinelibrary.controller.request;

import lombok.Data;

@Data
public class CopyBookRequest {
    private Integer bookId;
    private String isbn;
    private Integer number;
    private Integer libraryId;
}
