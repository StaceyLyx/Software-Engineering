package com.se.onlinelibrary.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.se.onlinelibrary.bean.Library;
import com.se.onlinelibrary.bean.book.Book;
import com.se.onlinelibrary.bean.book.Borrow;
import com.se.onlinelibrary.bean.book.Copybook;
import com.se.onlinelibrary.bean.user.Authority;
import com.se.onlinelibrary.bean.user.User;
import com.se.onlinelibrary.controller.request.CopyBookRequest;
import com.se.onlinelibrary.service.bookServices.*;
import com.se.onlinelibrary.service.userServices.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//图书操作
@Slf4j
@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    CopybookService copybookService;

    @Autowired
    BorrowService borrowService;

    @Autowired
    ReturnBookService returnBookService;

    @Autowired
    ReserveService reserveService;

    @Autowired
    LibraryService libraryService;

    @Autowired
    UserService userService;

    //图书分页展示
    @RequestMapping(value = "resources/books/{page}/{size}", method = RequestMethod.GET)
    public ResponseEntity<Page<Book>> findAll(@PathVariable(value = "page") Integer page,
                                              @PathVariable("size") Integer size) {
        Page<Book> queryPage = new Page<>(page, size);
        return ResponseEntity.ok()
                .body(bookService.page(queryPage, null));
    }

    @RequestMapping(value = "resources/books/totalNumber", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Integer>> totalNumber() {
        Map<String, Integer> map = new HashMap<>();
        map.put("totalNumber", bookService.list().size());
        return ResponseEntity.ok().body(map);
    }

    //书本上新
    @RequestMapping(value = "admin/book/upload", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> upload(Book book) {
        log.info("访问后端/book/upload：book信息为" + book);
        Map<String, Object> map = new HashMap<>();
        if(book == null){
            throw new BadCredentialsException("Book is not valid");
        }else {
            // 通过ISBN进行验证，ISBN不存在后添加到数据库并返回上新成功状态码
            // upload函数内进行了数据库查找验证，若ISBN存在则返回400状态码
            if (bookService.upload(book)) {
                // 上新成功，返回201（created）状态码
                Integer id = bookService.getIDByIsbn(book.getIsbn());
                map.put("bookId", id);
                return ResponseEntity.
                        status(201).body(map);
            } else {
                // 图书已存在，返回400状态码
                map.put("message", "Upload failed");
                return ResponseEntity.status(400).body(map);
            }
        }
    }

    // 书本更新：待完善
    @RequestMapping(value = "admin/book/update", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> updateBook(Book book) {
        log.info("访问后端admin/book/update：book信息为" + book);
        Map<String, Object> map = new HashMap<>();
        // 通过ISBN进行验证，ISBN存在后允许修改数据库并返回更改成功状态码
        // update函数内进行了数据库查找验证，若ISBN不存在则返回400状态码
        if (bookService.update(book)) {
            // 上新成功，返回.OK（200）状态码
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // 图书不存在，返回400状态码
            map.put("message", "update failed");
            return ResponseEntity.status(400).body(map);
        }
    }

    // 副本更新：待完善
    @RequestMapping(value = "admin/copybook/update", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> updateCopybook(Copybook copybook){
        log.info("访问后端admin/copybook/update：copybook信息为" + copybook);
        Map<String, Object> map = new HashMap<>();
        // 通过copyId进行验证，copyId存在后允许修改数据库并返回更改成功状态码
        if(copybookService.update(copybook)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            map.put("message", "update failed");
            return ResponseEntity.status(400).body(map);
        }
    }

    //图书上新"ISBN验证"
    @RequestMapping(value = "admin/book/upload/isbn", method = RequestMethod.POST)
    public ResponseEntity<Object> ifIsbnExist(String isbn) {
        log.info("访问后端/book/upload/isbn：isbn信息为" + isbn);
        Map<String, Object> map = new HashMap<>();
        if(isbn == null){
            throw new BadCredentialsException("isbn is not valid");
        }else {
            // 通过ISBN查询图书是否存在                // ISBN已存在，返回400状态码
            if (bookService.ifIsbnExists(isbn)) {
                log.info("ISBN存在！返回前端");
                map.put("message", "ISBN existed");
                return ResponseEntity.status(400).body(map);

                //status(400).body(map);
            } else {
                log.info("ISBN不存在！返回前端");
                // 不存在，返回.OK（200）状态码
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }

    // 图书详情展示界面（前端发送bookId）
    @RequestMapping(value = "resources/books/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<Object> detailOfBookId(@PathVariable(value = "bookId")Integer bookId){
        log.info("访问后端resources/books/{bookId}：bookId信息为" + bookId);
        Map<String, Object> map = new HashMap<>();
        if(bookId == null){
            throw new BadCredentialsException("bookId is not valid");
        }else {
            Book result = bookService.detailOfBookId(bookId);
            if (result != null) {
                map.put("result", result);
                return ResponseEntity.ok().body(map);
            } else {
                map.put("result", "search error");
                return ResponseEntity.status(400).body(map);
            }
        }
    }

    // 图书详情展示界面（前端发送ISBN）
    @RequestMapping(value = "resources/books/isbn", method = RequestMethod.POST)
    public ResponseEntity<Object> detailOfIsbn(String isbn){
        log.info("访问后端resources/books/{isbn}：isbn信息为" + isbn);
        Map<String, Object> map = new HashMap<>();
        if(isbn == null){
            throw new BadCredentialsException("isbn is not valid");
        }else {
            Book result = bookService.detailOfIsbn(isbn);
            if (result != null) {
                map.put("result", result);
                return ResponseEntity.ok().body(map);
            } else {
                map.put("result", "search error");
                return ResponseEntity.status(400).body(map);
            }
        }
    }

    // 得到对应bookId的所有副本信息
    @RequestMapping(value = "resources/AllCopybooks/{bookId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> allCopybooks(@PathVariable(value = "bookId")Integer bookId){
        log.info("访问后端resources/AllCopybooks/{bookId}：bookId信息为" + bookId);
        if(bookId == null){
            throw new BadCredentialsException("bookId is not valid");
        }else {
            List<Copybook> copybooks = copybookService.allCopybooks(bookId);
            Map<String, Object> map = new HashMap<>();
            if (copybooks.isEmpty()) {
                map.put("result", "search error");
                return ResponseEntity.status(400).body(map);
            } else {
                map.put("result", copybooks);
                return ResponseEntity.ok().body(map);
            }
        }
    }

    // 得到对应copyId的副本信息
    @RequestMapping(value = "account/resources/books/copybook", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getCopybook(Integer copyId){
        log.info("访问后端account/resources/books/copybook：copyId信息为" + copyId);
        Map<String, Object> map = new HashMap<>();
        if(copyId == null){
            throw new BadCredentialsException("copyId is not valid");
        }else {
            Copybook copybook = copybookService.getCopybook(copyId);
            if (copybook == null) {
                map.put("result", "copybook not exist");
                return ResponseEntity.status(400).body(map);
            } else {
                map.put("result", copybook);
                return ResponseEntity.ok().body(map);
            }
        }
    }

    // 得到对应copyId的副本信息和与其相关的所有信息
    @RequestMapping(value = "resources/books/copybook/allInfo", method = RequestMethod.POST)
    public ResponseEntity<Object> getAllCopybookInfo(Integer copyId){
        log.info("访问后端resources/books/copybook/allInfo：copyId信息为" + copyId);
        if(copyId == null){
            throw new BadCredentialsException("copyId is not valid");
        }else {
            Map<String, Object> map = new HashMap<>();
            Copybook copybook = copybookService.getCopybook(copyId);
            if (copybook == null) {
                map.put("message", "search error");
                return ResponseEntity.status(400).body(map);
            } else {
                // 查询得到目标副本，开始查找其他相关信息
                map.put("copybook", copybook);
                // 副本对应Book信息
                Book book = bookService.detailOfBookId(copybook.getBookId());
                map.put("book", book);
                // 副本对应Library信息
                Library library = libraryService.getLibrary(copybook.getLibraryId());
                map.put("library", library);
                return ResponseEntity.ok().body(map);
            }
        }
    }

    // 管理员添加图书副本（系统自动为副本生成ISBN）：管理员操作
    @RequestMapping(value = "admin/resources/books/addCopybook", method = RequestMethod.POST)
    public void copybook(CopyBookRequest copyBookRequest){
        log.info("访问后端resources/books/addCopybook：copyBookRequest信息为" + copyBookRequest);
        if(copyBookRequest.getBookId() == null || copyBookRequest.getNumber() == null
                || copyBookRequest.getIsbn() == null || copyBookRequest.getLibraryId() == null){
            throw new BadCredentialsException("some information is not valid");
        }else {
            copybookService.copyBook(copyBookRequest);
        }
    }

    // 现场借书检查是否存在图书：管理员操作
    @RequestMapping(value = "admin/resources/books/borrowCheck", method = RequestMethod.POST)
    public ResponseEntity<Object> borrowCheck(String isbn){
        log.info("访问后端admin/resources/books/borrowCheck：isbn信息为" + isbn);
        if(isbn == null){
            throw new BadCredentialsException("isbn is not valid");
        }else {
            Map<String, Object> map = new HashMap<>();
            Copybook copyBook = copybookService.borrowCheck(isbn);
            if (copyBook == null) {
                // 不允许借出：isbn不存在
                map.put("message", "isbn not exist");
                return ResponseEntity.status(404).body(map);
            } else if (!copyBook.getStatus().equals("空闲中")) {
                // 不允许借出：状态约束
                map.put("message", "wrong status");
                return ResponseEntity.status(406).body(map);
            } else {
                // 允许借出
                return ResponseEntity.ok().body(copyBook);
            }
        }
    }

    @RequestMapping(value = "admin/resources/books/borrowBook", method = RequestMethod.POST)
    public ResponseEntity<Object> borrowBook(Integer copyId, String username, Integer adminId, Integer libraryId){
        log.info("访问后端admin/resources/books/borrowBook：username信息为" + username);
        User user = userService.findUserByUserName(username);
        Integer userId = null;

        if (user != null) {
            List<Authority> authorities = userService.findAuthorityByUserId(user.getId());
            for (Authority authority : authorities)
                if (authority.getId() == 2 || authority.getId() == 3) // student or teacher
                    userId = authority.getId();
        }

        if (userId != null) {
            return borrowBook(copyId, userId, adminId, libraryId);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "no such username with authority of user(student or teacher)");
            return ResponseEntity.status(400).body(map);
        }
    }

    // 现场借书（检查成功后：查找图书存在）：管理员操作
    public ResponseEntity<Object> borrowBook(Integer copyId, Integer userId, Integer adminId, Integer libraryId){
        log.info("访问后端admin/resources/books/borrowBook：copyId信息为" + copyId);
        if(copyId == null || userId == null || adminId == null || libraryId == null){
            throw new BadCredentialsException("some information is not valid");
        }else {
            Map<String, Object> map = new HashMap<>();
            // if判断copybook是否存在与状态是否合法
            if (copybookService.borrowBook(copyId)) {
                // 合法的话就添加borrow记录
                borrowService.borrowBook(copyId, userId, adminId, libraryId); // 更新借阅记录
                map.put("message", "borrow succeed");
                return ResponseEntity.ok().body(map);
            } else {
                map.put("message", "borrow failed");
                return ResponseEntity.status(402).body(map);
            }
        }
    }

    // 现场还书检查，检查书籍是否被借：管理员操作
    @RequestMapping(value = "admin/resources/books/returnCheck", method = RequestMethod.POST)
    public ResponseEntity<Object> returnCheck(String isbn){
        log.info("访问后端admin/resources/books/returnCheck：isbn信息为" + isbn);
        if(isbn == null){
            throw new BadCredentialsException("isbn is not valid");
        }else {
            Map<String, Object> map = new HashMap<>();
            Copybook copyBook = copybookService.returnCheck(isbn);
            if (copyBook == null) {
                // 查找isbn不存在
                map.put("message", "isbn not exist");
                return ResponseEntity.status(404).body(map);
            } else if (!copyBook.getStatus().equals("已借出")) {
                // 查找副本的状态错误
                map.put("message", "wrong status");
                return ResponseEntity.status(400).body(map);
            } else {
                Borrow borrow = borrowService.returnCheck(copyBook.getId());
                if (borrow == null) {
                    // 没有该借阅记录
                    map.put("message", "search error");
                    return ResponseEntity.status(400).body(map);
                } else {
                    return ResponseEntity.ok().body(borrow);
                }
            }
        }
    }

    // 现场还书：管理员操作
    @RequestMapping(value = "admin/resources/books/returnBook", method = RequestMethod.POST)
    public ResponseEntity<Object> returnBook(Integer borrowId, Integer adminId, Integer libraryId, String isbn){
        log.info("访问后端/admin/resources/books/returnBook：isbn信息为：" + isbn);
        if(borrowId == null || adminId == null || libraryId == null || isbn == null){
            throw new BadCredentialsException("some information is not valid");
        }else {
            Map<String, Object> map = new HashMap<>();
            if (copybookService.returnBook(isbn, libraryId)) {   // 状态为“已借出”状态，可以被归还
                returnBookService.returnBook(borrowId, adminId, libraryId);
                map.put("message", "return succeed");
                return ResponseEntity.ok().body(map);
            } else {                                  // 状态非“已借出”状态或isbn查找不存在，不可以被归还
                map.put("message", "return failed");
                return ResponseEntity.status(404).body(map);
            }
        }
    }

    // 用户在线预约
    @RequestMapping(value = "user/book/reserve", method = RequestMethod.POST)
    public ResponseEntity<Object> reserve(Integer copyId, Integer userId){
        log.info("访问后端/user/book/reserve：copyId信息为" +  copyId);
        log.info("访问后端/user/book/reserve：userId信息为" +  userId);
        if(copyId == null || userId == null){
            throw new BadCredentialsException("copyId or userId is not valid");
        }else {
            Map<String, Object> map = new HashMap<>();
            if (!copybookService.reserveCheckOne(copyId)) { // 副本状态非空闲中或不存在该副本
                map.put("message", "wrong status or copyId");
                return ResponseEntity.status(400).body(map);
            } else if (!reserveService.reserveCheckTwo(userId)) { // 用户预约超过五本图书限制
                map.put("message", "number exception");
                return ResponseEntity.status(402).body(map);
            } else {
                reserveService.reserve(copyId, userId);
                copybookService.reserve(copyId);
                map.put("message", "reserve succeed");
                return ResponseEntity.ok().body(map);
            }
        }
    }

    // 用户查询预约信息
    @RequestMapping(value = "user/book/allReservation", method = RequestMethod.POST)
    public ResponseEntity<Object> allReservation(Integer userId){
        log.info("访问后端user/book/allReservation：userId信息为" + userId);
        if(userId == null){
            throw new BadCredentialsException("userId is not valid");
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("result", reserveService.allReservation(userId)); // 长度从0-5
            return ResponseEntity.ok().body(map);
        }
    }

    // 用户取消预约
    @RequestMapping(value = "user/book/cancel", method = RequestMethod.POST)
    public ResponseEntity<Object> cancelReservation(Integer userId, Integer copyId){
        log.info("访问后端user/book/cancel：userId信息为" + userId);
        log.info("访问后端user/book/cancel：copyId信息为" + copyId);
        if(userId == null || copyId == null){
            throw new BadCredentialsException("userId or copyId is not valid");
        }else {
            Map<String, Object> map = new HashMap<>();
            if (reserveService.cancelCheck(userId, copyId)) {
                copybookService.cancelReserve(copyId);
                map.put("message", "cancel succeed");
                return ResponseEntity.ok().body(map);
            } else {   // 不存在此预约信息或不是已预约状态
                map.put("message", "cancel failed");
                return ResponseEntity.status(400).body(map);
            }
        }
    }

    // 续借功能（暂不实现）
    @RequestMapping(value = "user/book/renew", method = RequestMethod.PUT)
    public void renew(){
    }

    // 查询所有已预约未取书的预约信息
    @RequestMapping(value = "admin/resources/books/reservationCheck", method = RequestMethod.POST)
    public ResponseEntity<Object> reservationCheck(String username){
        log.info("访问后端admin/resources/books/reservationCheck：username信息为" + username);
        Map<String, Object> map = new HashMap<>();
        if(username == null){
            throw new BadCredentialsException("username is not valid");
        }else {
            if(userService.findUserByUserName(username) == null){
                map.put("message", "user not exist");
                return ResponseEntity.status(400).body(map);
            }else {
                map.put("record", reserveService.reservationCheck(userService.findUserByUserName(username).getId()));
                return ResponseEntity.ok().body(map);
            }
        }
    }

    // 根据预约信息借书
    @RequestMapping(value = "admin/resources/books/reservationBorrow", method = RequestMethod.POST)
    public ResponseEntity<Object> reservationBorrow(Integer reserveId, Integer copyId, Integer userId, Integer adminId, Integer libraryId){
        log.info("访问后端admin/resources/books/reservationBorrow：reserveId信息为" +  reserveId);
        if(reserveId == null || copyId == null || userId == null || adminId == null || libraryId == null){
            throw new BadCredentialsException("some information is not valid");
        }else {
            // 检测是否可以取走
            Map<String, Object> map = new HashMap<>();
            // 1. 副本状态是否为已预约
            // 2. 预约Reserve表中的reserveId是否与userId对应
            if (copybookService.reservationBorrowCheckOne(copyId) && reserveService.reservationBorrowCheckTwo(reserveId, userId)) {
                // 预约成功
                copybookService.reservationBorrow(copyId); // 更改副本信息
                borrowService.borrowBook(userId, copyId, adminId, libraryId); // 记录借阅信息
                map.put("message", "borrow succeed");
                return ResponseEntity.ok().body(map);
            } else if (!copybookService.reservationBorrowCheckOne(copyId)) {
                // 副本id不对或状态不是已预约
                map.put("message", "wrong copyId or status");
                return ResponseEntity.status(400).body(map);
            } else if (!reserveService.reservationBorrowCheckTwo(reserveId, userId)) {
                // 不存在该预约信息或预约信息与预约人信息不符
                map.put("message", "reservation not exist or wrong userId");
                return ResponseEntity.status(404).body(map);
            } else {
                // 其他错误
                map.put("message", "other error");
                return ResponseEntity.status(422).body(map);
            }
        }
    }

    // 用户字符串模糊查询
    @RequestMapping(value = "resources/searchBook/{searchString}", method = RequestMethod.GET)
    public ResponseEntity<Object> searchBook(@PathVariable(value = "searchString") String searchString){
        Map<String, Object> map = new HashMap<>();
        if(searchString == null){
            log.info("访问后端resources/books/searchBook/{searchString}：searchString信息为null");
            throw new BadCredentialsException("searchString is not valid");
        }else{
            searchString = new String(searchString.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            log.info("访问后端resources/books/searchBook/{searchString}：searchString信息为" + searchString);
            List<Book> books = new LinkedList<>();
            // 在isbn字段中查找
            books.addAll(bookService.findBookBySearchStringFromIsbn(searchString));
            // 在authorName字段中查找
            books.addAll(bookService.findBookBySearchStringFromAuthorName(searchString));
            // 在title字段中查找
            books.addAll(bookService.findBookBySearchStringFromTitle(searchString));
            if(books.isEmpty()){
                map.put("result", "no search result");
                return ResponseEntity.status(400).body(map);
            }else {
                map.put("result", books);
                return ResponseEntity.ok().body(map);
            }
        }
    }
}
