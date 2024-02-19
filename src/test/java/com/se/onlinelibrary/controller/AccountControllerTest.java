package com.se.onlinelibrary.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class AccountControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;

    @BeforeEach
    public void setUpMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAllUserInfoRight() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/account/getAllUserInfo")
                .accept(MediaType.APPLICATION_JSON).param("username","admin"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();
        log.info(mvcResult.getResponse().getContentAsString());
    }
    @Test
    void getAllUserInfoUserNotExist() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/account/getAllUserInfo")
        .accept(MediaType.APPLICATION_JSON).param("username","18307111234"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("User doesn't exist"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    @Test
    void getAllUserInfoLackUsernameArg() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/account/getAllUserInfo")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username is not valid"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Transactional //事务回滚，不会操作数据库
    void modifyPasswordRight() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/account/modifyPassword")
                .accept(MediaType.APPLICATION_JSON).param("username","18307113333")
                .param("oldPassword","pass12345").param("newPassword","pass1234"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    @Test
    @Transactional //事务回滚，不会操作数据库
    void modifyPasswordLackArgs() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/account/modifyPassword")
                .accept(MediaType.APPLICATION_JSON).param("username","18307113333")
                .param("oldPassword","pass12345"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("ModifyRequest is not legal"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    @Test
    @Transactional //事务回滚，不会操作数据库
    void modifyPasswordOldNewPasswordSame() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/account/modifyPassword")
                .accept(MediaType.APPLICATION_JSON).param("username","18307113333")
                .param("oldPassword","pass12345").param("newPassword","pass12345"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("New password is the same as the old password"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    @Test
    @Transactional //事务回滚，不会操作数据库
    void modifyPasswordNewPasswordContainUsername() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/account/modifyPassword")
                .accept(MediaType.APPLICATION_JSON).param("username","18307113333")
                .param("oldPassword","pass12345").param("newPassword","pass18307113333"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password shouldn't contain username"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    @Test
    @Transactional //事务回滚，不会操作数据库
    void modifyPasswordNewPassword2LongShortContainSpecialLetter() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/account/modifyPassword")
                .accept(MediaType.APPLICATION_JSON).param("username","18307113333")
                .param("oldPassword","pass12345").param("newPassword","pass1"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password contains illegal signature or too long or too short"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        mvc.perform(MockMvcRequestBuilders.post("/account/modifyPassword")
                .accept(MediaType.APPLICATION_JSON).param("username","18307113333")
                .param("oldPassword","pass12345").param("newPassword","pass123456789012345678901234567890"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password contains illegal signature or too long or too short"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        mvc.perform(MockMvcRequestBuilders.post("/account/modifyPassword")
                .accept(MediaType.APPLICATION_JSON).param("username","18307113333")
                .param("oldPassword","pass12345").param("newPassword","pass1234#"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password contains illegal signature or too long or too short"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Transactional //事务回滚，不会操作数据库
    void modifyPasswordNewPasswordTooSimple() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/account/modifyPassword")
                .accept(MediaType.APPLICATION_JSON).param("username","18307113333")
                .param("oldPassword","pass12345").param("newPassword","passss"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Password's security is bad"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Transactional //事务回滚，不会操作数据库
    void modifyPasswordUserNotExist() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/account/modifyPassword")
                .accept(MediaType.APPLICATION_JSON).param("username","18307111234")
                .param("oldPassword","pass12345").param("newPassword","pass1234"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("User doesn't exist"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Transactional //事务回滚，不会操作数据库
    void modifyPasswordOldPasswordWrong() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/account/modifyPassword")
                .accept(MediaType.APPLICATION_JSON).param("username","admin")
                .param("oldPassword","pass123456").param("newPassword","pass1234"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Old password is not correct"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}