package com.se.onlinelibrary.service.bookServices;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.onlinelibrary.bean.book.Copybook;
import com.se.onlinelibrary.controller.request.CopyBookRequest;

import java.util.List;

public interface CopybookService extends IService<Copybook> {
    List<Copybook> allCopybooks(int bookId);
    Copybook getCopybook(int copyId);

    Copybook borrowCheck(String isbn);
    boolean borrowBook(int copyId);

    Copybook returnCheck(String isbn);
    boolean returnBook(String isbn, int libraryId);

    void copyBook(CopyBookRequest copyBookRequest);

    boolean reserveCheckOne(int copyId);
    void reserve(int copyId);

    void cancelReserve(int copyId);

    boolean reservationBorrowCheckOne(int copyId);
    void reservationBorrow(int copyId);

    boolean update(Copybook copybook);
}
