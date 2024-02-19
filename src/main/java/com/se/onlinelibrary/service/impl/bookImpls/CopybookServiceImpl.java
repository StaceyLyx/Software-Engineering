package com.se.onlinelibrary.service.impl.bookImpls;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.onlinelibrary.bean.book.Copybook;
import com.se.onlinelibrary.bean.user.User;
import com.se.onlinelibrary.controller.request.CopyBookRequest;
import com.se.onlinelibrary.mapper.CopyBookMapper;
import com.se.onlinelibrary.service.bookServices.CopybookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

@Slf4j
@Service
public class CopybookServiceImpl extends ServiceImpl<CopyBookMapper, Copybook> implements CopybookService {
    // 更改已存在副本信息
    public boolean update(Copybook copybook){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("id", copybook.getId());
        Copybook findCopybook = getOne(queryWrapper);
        if(findCopybook == null){
            return false;
        }else{
            findCopybook = copybook;
            update(findCopybook, new QueryWrapper<Copybook>().lambda().eq(Copybook::getId, findCopybook.getId()));
            return true;
        }
    }

    // 详情页查找所有图书信息
    public List<Copybook> allCopybooks(int bookId){
        QueryWrapper<Copybook> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Copybook::getBookId, bookId);
        return list(queryWrapper);
    }

    // 得到对应copyId的副本信息
    public Copybook getCopybook(int copyId){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("id", copyId);
        return getOne(queryWrapper);
    }

    // 现场借书条件检查
    public Copybook borrowCheck(String isbn){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("isbn", isbn);
        return getOne(queryWrapper);
    }

    // 现场借书：更改图书状态
    public boolean borrowBook(int copyId){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("id", copyId);
        Copybook findCopybook = getOne(queryWrapper);
        if(findCopybook == null || !findCopybook.getStatus().equals("空闲中")) {
            return false;
        }else{
            findCopybook.setStatus("已借出");
            update(findCopybook, new QueryWrapper<Copybook>().lambda().eq(Copybook::getId, copyId));
            return true;  // 可以借阅
        }
    }

    // 现场还书条件检查
    public Copybook returnCheck(String isbn){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("isbn", isbn);
        return getOne(queryWrapper);
    }

    // 现场还书
    public boolean returnBook(String isbn, int libraryId){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("isbn", isbn);
        Copybook findCopybook = getOne(queryWrapper);
        if(findCopybook == null){
            // 查找不存在该图书
            return false;
        }else if(!findCopybook.getStatus().equals("已借出")){
            // 图书不是借出状态，无法归还
            return false;
        }else{
            // 归还成功
            findCopybook.setStatus("空闲中");
            findCopybook.setLibraryId(libraryId);
            update(findCopybook, new QueryWrapper<Copybook>().lambda().eq(Copybook::getIsbn, isbn));
            return true;
        }
    }

    // 管理员添加图书副本（系统自动为副本生成ISBN）
    public void copyBook(CopyBookRequest copyBookRequest){
        int bookId = copyBookRequest.getBookId();
        String isbn = copyBookRequest.getIsbn();
        int number = copyBookRequest.getNumber();
        int libraryId = copyBookRequest.getLibraryId();

        QueryWrapper<Copybook> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Copybook::getBookId, bookId);
        List<Copybook> copybookList = list(queryWrapper);

        int copyBookNumber = copybookList.size();
        Format format = new DecimalFormat("000");
        for(int i = copyBookNumber + 1; i <= number + copyBookNumber; ++i){
            Copybook copyBook = new Copybook();
            copyBook.setBookId(bookId);
            copyBook.setIsbn(isbn + "-" + format.format(i));
            copyBook.setStatus("空闲中");
            copyBook.setLibraryId(libraryId);
            save(copyBook);
        }
    }

    // 检查借阅信息
    public boolean reserveCheckOne(int copyId){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("id", copyId);
        Copybook findCopybook = getOne(queryWrapper);
        return findCopybook != null && findCopybook.getStatus().equals("空闲中");
    }

    // 预约修改副本状态
    public void reserve(int copyId){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("id", copyId);
        Copybook findCopybook = getOne(queryWrapper);
        findCopybook.setStatus("被预约");
        update(findCopybook, new QueryWrapper<Copybook>().lambda().eq(Copybook::getId, copyId));
    }

    // 删除预约记录：更改副本信息
    public void cancelReserve(int copyId){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("id", copyId);
        Copybook findCopybook = getOne(queryWrapper);
        findCopybook.setStatus("空闲中");
        update(findCopybook, new QueryWrapper<Copybook>().lambda().eq(Copybook::getId, copyId));
    }

    // 预约取书：检测是否是预约状态
    public boolean reservationBorrowCheckOne(int copyId){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("id", copyId);
        Copybook findCopybook = getOne(queryWrapper);
        if(findCopybook != null){
            return findCopybook.getStatus().equals("已预约");
        }else {
            return false;
        }
    }

    // 预约取书：更改副本状态
    public void reservationBorrow(int copyId){
        Wrapper<Copybook> queryWrapper = new QueryWrapper<Copybook>().eq("id", copyId);
        Copybook findCopybook = getOne(queryWrapper);
        findCopybook.setStatus("已借出");
        update(findCopybook, new QueryWrapper<Copybook>().lambda().eq(Copybook::getId, copyId));
    }
}

