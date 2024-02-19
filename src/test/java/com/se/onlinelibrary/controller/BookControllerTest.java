package com.se.onlinelibrary.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
// 数据库事务回滚
@Transactional
class BookControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    static String path = "http://localhost:8181/";

    // 实例化MockMvc
    @BeforeEach
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    // 分页查询测试
    @Test
    public void findAll() throws Exception{
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(path + "resources/books/6/10")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    // 返回所有书本数目
    @Test
    public void totalNumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "resources/books/totalNumber"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    // 上新图书
    @Test
    public void upload() throws Exception{
        // 图书ISBN不存在
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/book/upload")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "0000000000000")
                .param("authorName", "xxx")
                .param("coverUrl", "url")
                .param("description", "description")
                .param("publishTime", "2021-1-1")
                .param("title", "title"))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // 图书ISBN存在
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/book/upload")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "9787544722766")
                .param("authorName", "xxx")
                .param("coverUrl", "url")
                .param("description", "description")
                .param("publishTime", "2021-1-1")
                .param("title", "title"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Upload failed"))
                .andReturn();
    }

    @Test
    public void update() {
    }


    @Test
    public void ifIsbnExist() throws Exception{
        // ISBN传入为空
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/book/upload/isbn")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("isbn is not valid"))
                .andReturn();

        // ISBN不存在
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/book/upload/isbn")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "0000000000000"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        // ISBN存在
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/book/upload/isbn")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "9787020015092"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("ISBN existed"))
                .andReturn();
    }

    @Test
    public void detailOfBookId() throws Exception{

        // bookId存在
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(path + "resources/books/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // bookId不存在
        mockMvc.perform(MockMvcRequestBuilders
                .get(path + "resources/books/300")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("search error"))
                .andReturn();

        // bookId传入为空
//        mockMvc.perform(MockMvcRequestBuilders
//                .get(path + "resources/books")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(MockMvcResultMatchers.status().is(403))
//                .andExpect(MockMvcResultMatchers.jsonPath("result").value("bookId is not valid"));
    }

    @Test
    public void allCopybooks() throws Exception{

        // bookId存在的副本
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(path + "resources/AllCopybooks/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // bookId不存在的副本
        mockMvc.perform(MockMvcRequestBuilders
                .get(path + "resources/AllCopybooks/300")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("search error"))
                .andReturn();

        // bookId传入为空
//        mockMvc.perform(MockMvcRequestBuilders
//                .get(path + "resources/AllCopybooks")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(MockMvcResultMatchers.status().is(403))
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("bookId is not valid"));
    }

    @Test
    public void getCopybook() throws Exception{

        // 对应copyId的副本存在
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(path + "account/resources/books/copybook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "1"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // 对应copyId的副本不存在
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "account/resources/books/copybook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "100"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("result").value("copybook not exist"))
                .andReturn();

        // copyId传入为空
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "account/resources/books/copybook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("copyId is not valid"))
                .andReturn();
    }

    @Test
    public void getAllCopybookInfo() throws Exception{

        // 对应copyId存在的副本
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(path + "resources/books/copybook/allInfo")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "1"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // 对应copyId不存在的副本
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "resources/books/copybook/allInfo")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "100"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("search error"))
                .andReturn();

        // copyId传入为空
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "resources/books/copybook/allInfo")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("copyId is not valid"))
                .andReturn();
    }

    @Test
    public void copybook() throws Exception{

        // 管理员添加图书副本：添加成功
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/addCopybook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("bookId", "1")
                .param("isbn", "9787020002207")
                .param("number", "5")
                .param("libraryId", "1"))
                .andReturn();

        // 管理员添加图书副本：有信息缺失
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/addCopybook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("number", "5")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("some information is not valid"))
                .andReturn();
    }

    @Test
    public void borrowCheck() throws Exception{

        // 现场借书检查：isbn不存在
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/borrowCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "0000000000000"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("isbn not exist"))
                .andReturn();

        // 现场借书检查：状态约束
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/borrowCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "9787020002207-1"))
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("wrong status"))
                .andReturn();

        // 现场借书检查：允许借出
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/borrowCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "9787020002207-2"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // 现场借书检查：isbn传入为空
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/borrowCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("isbn is not valid"))
                .andReturn();
    }

    @Test
    public void borrowBook() throws Exception{

        // 现场借书：copyId不对
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/borrowBook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "100")
                .param("userId", "1")
                .param("adminId", "1")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(402))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("borrow failed"))
                .andReturn();

        // 现场借书：状态不对
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/borrowBook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "1")
                .param("userId", "1")
                .param("adminId", "1")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(402))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("borrow failed"))
                .andReturn();

        // 现场借书：有信息为空
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/borrowBook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "100")
                .param("adminId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("some information is not valid"))
                .andReturn();

        // 现场借书：借书成功
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/borrowBook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "2")
                .param("userId", "1")
                .param("adminId", "1")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("borrow succeed"))
                .andReturn();
    }

    @Test
    public void returnCheck() throws Exception{

        // 现场还书检查：查找isbn不存在
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/returnCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "0000000000"))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("isbn not exist"))
                .andReturn();

        // 现场还书检查：查找副本的状态非“已借出”
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/returnCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "9787020002207-2"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("wrong status"))
                .andReturn();

        // 现场还书检查：查找没有该借阅记录
//      mockMvc.perform(MockMvcRequestBuilders
//                .post(path + "admin/resources/books/returnCheck")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("isbn", "9787020002207-2"))
//                .andExpect(MockMvcResultMatchers.status().is(400))
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("search error"));

        // 现场还书检查：归还成功
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/returnCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "9787020002207-1"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // 现场还书检查：isbn传入为空
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/returnCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("isbn is not valid"))
                .andReturn();
    }

    @Test
    public void returnBook() throws Exception{

        // 现场还书：有信息缺失
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/returnBook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "0000000000000"))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("some information is not valid"))
                .andReturn();

        // 现场还书：ISBN查找不存在
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/returnBook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "0000000000000")
                .param("borrowId", "1")
                .param("adminId", "1")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("return failed"))
                .andReturn();

        // 现场还书：副本状态非已借出
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/returnBook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("isbn", "9787020002207-2")
                .param("borrowId", "1")
                .param("adminId", "1")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("return failed"))
                .andReturn();

        // 现场还书：可以归还
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/returnBook")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("borrowId", "1")
                .param("adminId", "1")
                .param("libraryId", "1")
                .param("isbn", "9787020002207-1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("return succeed"))
                .andReturn();
    }

    @Test
    public void reserve() throws Exception{

        // 在线预约：有信息缺失
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "user/book/reserve")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("copyId or userId is not valid"))
                .andReturn();

        // 在线预约：不存在该副本
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "user/book/reserve")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "100")
                .param("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("wrong status or copyId"))
                .andReturn();

        // 在线预约：状态不对
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "user/book/reserve")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "1")
                .param("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("wrong status or copyId"))
                .andReturn();

        // 在线预约：用户预约超额
        // TODO：测试用例
//        mockMvc.perform(MockMvcRequestBuilders
//                .post(path + "user/book/reserve")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("copyId", "1")
//                .param("userId", "1"))
//                .andExpect(MockMvcResultMatchers.status().is(402))
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("number exception"));

        // 在线预约：预约成功
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "user/book/reserve")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("copyId", "2")
                .param("userId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("reserve succeed"))
                .andReturn();
    }

    @Test
    public void allReservation() throws Exception{

        // 用户查询预约信息：传入userId为空
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "user/book/allReservation")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("userId is not valid"))
                .andReturn();

        // 用户查询预约信息：查询成功
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(path + "user/book/allReservation")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", "3"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void cancelReservation() throws Exception{

        // 用户取消预约：传入信息缺失
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "user/book/cancel")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("userId or copyId is not valid"))
                .andReturn();

        // 用户取消预约：不存在该预约信息
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "user/book/cancel")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", "3")
                .param("copyId", "2"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("cancel failed"))
                .andReturn();

        // 用户取消预约：取消成功
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "user/book/cancel")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("userId", "3")
                .param("copyId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("cancel succeed"))
                .andReturn();
    }

    @Test
    public void renew() {
    }

    @Test
    public void reservationCheck() throws Exception {

        // 查询所有已预约未取书的预约信息：用户名为空
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/reservationCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("username is not valid"))
                .andReturn();

        // 查询所有已预约未取书的预约信息：用户名不存在
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/reservationCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "0000000000"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("user not exist"))
                .andReturn();

        // 查询所有已预约未取书的预约信息：用户名存在
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/reservationCheck")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "18307114444"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void reservationBorrow() throws Exception{

        // 根据预约信息借书：有任何信息为空
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/reservationBorrow")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("reserveId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("some information is not valid"))
                .andReturn();

        // 根据预约信息借书：副本id不对
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/reservationBorrow")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("reserveId", "1")
                .param("copyId", "100")
                .param("userId", "1")
                .param("adminId", "1")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("wrong copyId or status"))
                .andReturn();

        // 根据预约信息借书：副本状态不对
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/reservationBorrow")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("reserveId", "1")
                .param("copyId", "1")
                .param("userId", "1")
                .param("adminId", "1")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("wrong copyId or status"))
                .andReturn();

        // 根据预约信息借书：不存在该预约信息
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/reservationBorrow")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("reserveId", "1")
                .param("copyId", "2")
                .param("userId", "1")
                .param("adminId", "1")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // 根据预约信息借书：预约信息与用户id不符
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/reservationBorrow")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("reserveId", "2")
                .param("copyId", "2")
                .param("userId", "3")
                .param("adminId", "1")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        // 根据预约信息借书：取书成功
        mockMvc.perform(MockMvcRequestBuilders
                .post(path + "admin/resources/books/reservationBorrow")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("reserveId", "4")
                .param("copyId", "3")
                .param("userId", "1")
                .param("adminId", "1")
                .param("libraryId", "1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("borrow succeed"))
                .andReturn();
    }

    @Test
    public void searchBook() throws Exception{

//        // 用户字符串模糊查询：非中文字符
//        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
//                .get(path + "resources/books/searchBook/9787")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED));
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();

        // 用户字符串模糊查询：中文字符
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get(path + "resources/books/searchBook/红")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}