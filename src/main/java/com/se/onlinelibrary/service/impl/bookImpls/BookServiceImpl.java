package com.se.onlinelibrary.service.impl.bookImpls;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.se.onlinelibrary.bean.Library;
import com.se.onlinelibrary.bean.book.Book;
import com.se.onlinelibrary.bean.book.Copybook;
import com.se.onlinelibrary.controller.request.CopyBookRequest;
import com.se.onlinelibrary.mapper.BookMapper;
import com.se.onlinelibrary.service.bookServices.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    // 上传图书
    public boolean upload(Book book){
        String ISBNLegal = "^[0-9]{13}+$";   // 要求ISBN为10位或13位的数字
        if(!ifIsbnExists(book.getIsbn()) && Pattern.matches(ISBNLegal, book.getIsbn())){
            return save(book);
        }else {
            return false;
        }
    }

    // 更新图书信息
    public boolean update(Book book){
        Wrapper<Book> queryWrapper = new QueryWrapper<Book>().eq("isbn", book.getIsbn());
        Book findBook = getOne(queryWrapper);
        if(findBook == null){
            return false;
        }else{
            findBook = book;
            update(findBook, new QueryWrapper<Book>().lambda().eq(Book::getIsbn, findBook.getIsbn()));
            return true;
        }
    }

    // 检验图书上新ISBN
    public boolean ifIsbnExists(String isbn){
        Wrapper<Book> queryWrapper = new QueryWrapper<Book>().eq("isbn",isbn);
        Book findBook = getOne(queryWrapper);
        return findBook != null;
    }

    // 检验图书上新ISBN
    public Integer getIDByIsbn(String isbn){
        Wrapper<Book> queryWrapper = new QueryWrapper<Book>().eq("isbn",isbn);
        Book findBook = getOne(queryWrapper);
        return findBook.getBookId();
    }

    // 图书详情展示页（bookId）
    public Book detailOfBookId(int bookId){
        Wrapper<Book> queryWrapper = new QueryWrapper<Book>().eq("book_id", bookId);
        return getOne(queryWrapper);
    }

    // 图书详情展示页（isbn）
    public Book detailOfIsbn(String isbn){
        Wrapper<Book> queryWrapper = new QueryWrapper<Book>().eq("isbn", isbn);
        return getOne(queryWrapper);
    }

    // 用户模糊查询：isbn
    public List<Book> findBookBySearchStringFromIsbn(String searchString){
        searchString = "%" + searchString + "%";
        return this.baseMapper.findBookBySearchStringFromIsbn(searchString);
    }

    // 用户模糊查询：authorName
    public List<Book> findBookBySearchStringFromAuthorName(String searchString){
        searchString = "%%" + searchString + "%%";
        return this.baseMapper.findBookBySearchStringFromAuthorName(searchString);
    }

    // 用户模糊查询：title
    public List<Book> findBookBySearchStringFromTitle(String searchString){
        searchString = "%%" + searchString + "%%";
        return this.baseMapper.findBookBySearchStringFromTitle(searchString);
    }
}
