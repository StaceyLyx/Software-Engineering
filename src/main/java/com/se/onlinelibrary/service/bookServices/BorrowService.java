package com.se.onlinelibrary.service.bookServices;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.onlinelibrary.bean.book.Borrow;

// 管理 记录借阅信息表
public interface BorrowService extends IService<Borrow> {
    void borrowBook(int userId, int copyId, int adminId, int libraryId);
    Borrow returnCheck(int copyId);
}
