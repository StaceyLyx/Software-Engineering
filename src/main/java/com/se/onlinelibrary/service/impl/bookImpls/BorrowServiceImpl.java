package com.se.onlinelibrary.service.impl.bookImpls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.onlinelibrary.bean.book.Borrow;
import com.se.onlinelibrary.mapper.BorrowMapper;
import com.se.onlinelibrary.service.bookServices.BorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService {

    // 现场借书：更新借阅记录
    public void borrowBook(int copyId, int userId, int adminId, int libraryId){

        Borrow borrowBook = new Borrow();
        borrowBook.setCopyId(copyId);
        borrowBook.setUserId(userId);
        borrowBook.setAdminId(adminId);
        borrowBook.setLibraryId(libraryId);

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        borrowBook.setBorrowDate(timestamp);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        calendar.add(Calendar.MONTH, 1);
        Timestamp returnTime = new Timestamp(calendar.getTimeInMillis());
        borrowBook.setDateToReturn(returnTime);

        save(borrowBook);
    }

    // 现场还书检查
    public Borrow returnCheck(int copyId){
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Borrow::getCopyId, copyId);
        List<Borrow> borrowList = list(queryWrapper);
        if(borrowList.isEmpty()){
            return null;
        }else {
            return borrowList.get(borrowList.size() - 1);
        }
    }
}


