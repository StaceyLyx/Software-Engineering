package com.se.onlinelibrary;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.se.onlinelibrary.bean.Library;
import com.se.onlinelibrary.bean.book.Copybook;
import com.se.onlinelibrary.bean.user.Authority;
import com.se.onlinelibrary.bean.book.Book;
import com.se.onlinelibrary.bean.user.User;
import com.se.onlinelibrary.mapper.UserMapper;
import com.se.onlinelibrary.service.bookServices.BookService;
import com.se.onlinelibrary.service.bookServices.CopybookService;
import com.se.onlinelibrary.service.bookServices.LibraryService;
import com.se.onlinelibrary.service.userServices.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Calendar;
import java.util.List;

@Slf4j
@SpringBootTest
class OnlinelibraryApplicationTests {
    @Autowired
    UserMapper userMapper;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    LibraryService libraryService;
    @Autowired
    CopybookService copyBookService;


    @Test
    void contextLoads() {
//        jdbcTemplate.queryForList("select * from user");
        Long numbers = jdbcTemplate.queryForObject("select count(*) from user", Long.class);
        log.info("记录数:{}", numbers);
    }

    @Test
    void testUserMapper() {
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
        User user = userMapper.selectById(1L);
        log.info("用户信息：{}", user);
    }

    @Test
    void testUserService() {
        List<User> userList = userService.list();
        userList.forEach(System.out::println);
    }

    @Test
    void testBookService() {
        List<Book> bookList = bookService.list();
        bookList.forEach(System.out::println);
    }


    @Test
    void jwtTest() {
        Calendar expireTime = Calendar.getInstance();
        expireTime.add(Calendar.SECOND, 2000);
        String token = JWT.create().withClaim("id", 1)
                .withClaim("user", "xiaohei")
                .withExpiresAt(expireTime.getTime())
                .sign(Algorithm.HMAC256("qwr?><sw"));

        System.out.println(token);
    }

    @Test
    void verityJwt() {
        //创建验证对象
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("qwr?><sw")).build();
        DecodedJWT verify = verifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwiZXhwIjoxNjE2NDE1NTk1LCJ1c2VyIjoieGlhb2hlaSJ9.E8mnkq8vgtvZK4fVEKgR-twcA7T3hLOCRZXayJV4uZQ");


        System.out.println(verify.getClaim("id").asInt());
        System.out.println(verify.getClaim("user").asString());
    }

    @Test
    void testBookServiceTwo() {
        Book book = new Book();
        book.setBookId(1);
        book.setTitle("呐喊");
        book.setAuthorName("鲁迅");
        book.setIsbn("ABC");
        book.setDescription("文学作品");
        book.setPublishTime("2021-3-25");
        book.setCoverUrl("ABC");
        System.out.println(bookService.save(book));
    }

    @Test
    void textFindRoleByUserId() {
        List<Authority> authorities = userService.findAuthorityByUserId(4);
        authorities.forEach(System.out::println);
    }

    @Test
    void testLibraryAll() {
        List<Library> libraryList = libraryService.list();
        System.out.println(libraryList);
    }

    @Test
    void testAllCopyBooks(){
        List<Copybook> copybookList = copyBookService.allCopybooks(1);
        System.out.println(copybookList);
    }

}
