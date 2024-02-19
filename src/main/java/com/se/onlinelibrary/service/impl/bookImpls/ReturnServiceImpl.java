package com.se.onlinelibrary.service.impl.bookImpls;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.onlinelibrary.bean.book.ReturnBook;
import com.se.onlinelibrary.mapper.ReturnBookMapper;
import com.se.onlinelibrary.service.bookServices.ReturnBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Slf4j
@Service
public class ReturnServiceImpl extends ServiceImpl<ReturnBookMapper, ReturnBook> implements ReturnBookService {

    // 现场还书：更新归还记录
    public void returnBook(int borrowId, int adminId, int libraryId){

        ReturnBook returnBook = new ReturnBook();
        returnBook.setBorrowId(borrowId);
        returnBook.setAdminId(adminId);
        returnBook.setLibraryId(libraryId);

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        returnBook.setTime(timestamp);

        save(returnBook);
    }
}



