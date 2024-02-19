package com.se.onlinelibrary.service.bookServices;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.onlinelibrary.bean.book.Book;
import com.se.onlinelibrary.bean.book.Reserve;
import com.se.onlinelibrary.controller.request.CopyBookRequest;

import java.util.List;

public interface ReserveService extends IService<Reserve> {
    boolean reserveCheckTwo(int userId);
    void reserve(int copyId, int userId);
    List<Reserve> allReservation(int userId);
    List<Reserve> reservationCheck(int userId);
    boolean cancelCheck(int userId, int copyId);
    boolean reservationBorrowCheckTwo(int reserveId, int userId);
}