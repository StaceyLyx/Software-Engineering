package com.se.onlinelibrary.service.bookServices;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.onlinelibrary.bean.book.Book;
import com.se.onlinelibrary.controller.request.CopyBookRequest;

import java.util.List;

public interface BookService extends IService<Book> {
    boolean upload(Book book);
    boolean update(Book book);
    boolean ifIsbnExists(String isbn);
    Integer getIDByIsbn(String isbn);
    Book detailOfBookId(int bookId);
    Book detailOfIsbn(String isbn);
    List<Book> findBookBySearchStringFromIsbn(String searchString);
    List<Book> findBookBySearchStringFromAuthorName(String searchString);
    List<Book> findBookBySearchStringFromTitle(String searchString);
}
