package com.se.onlinelibrary.service.bookServices;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.onlinelibrary.bean.book.ReturnBook;

// 管理 记录借阅信息表
public interface ReturnBookService extends IService<ReturnBook> {
    void returnBook(int borrowId, int adminId, int libraryId);
}
