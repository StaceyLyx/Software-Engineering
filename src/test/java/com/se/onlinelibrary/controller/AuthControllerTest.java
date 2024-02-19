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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class AuthControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mvc;

    @BeforeEach
    public void setUpMockMvc() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void ifUsernameExistDontMatchStudentId() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/register/username")
                .accept(MediaType.APPLICATION_JSON).param("username","1111"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username '1111' is not legal"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        mvc.perform(MockMvcRequestBuilders.post("/register/username")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username is not legal"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void ifUsernameExistHasRegisteredSomeAccount() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/register/username")
                .accept(MediaType.APPLICATION_JSON).param("username","18307113333"))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Hint:This account '18307113333' has registered as 'teacher', please use original password for register"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Transactional
    void ifUsernameExistTotalNewUser() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/register/username")
                .accept(MediaType.APPLICATION_JSON).param("username","18307110000"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Transactional
    void registerLackArgs() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/register/submit")
                .accept(MediaType.APPLICATION_JSON).param("username","18307112222"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Request id not legal"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Transactional
    void registerAuthorityArgWrong() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/register/submit")
                .accept(MediaType.APPLICATION_JSON).param("username","18307110000")
                .param("password","pass1234").param("authority","me")
                .param("email","18307112222@fudan.edu.cn"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Authority 'me' is not legal"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Transactional
    void registerSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/register/submit")
                .accept(MediaType.APPLICATION_JSON).param("username","18307110000")
                .param("password","pass1234").param("authority","student")
                .param("email","18307110000@fudan.edu.cn"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Authority 'me' is not legal"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void ifUsernameExist_aminHasRegistered() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/superAdmin/addAdmin/username")
                .accept(MediaType.APPLICATION_JSON).param("username","admin"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Warn:This account 'admin' has registered as admin"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void ifUsernameExist_aminSuccess() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/superAdmin/addAdmin/username")
                .accept(MediaType.APPLICATION_JSON).param("username","admin1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }



    @Test
    @Transactional
    void register_adminAuthorityWrong() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/superAdmin/addAdmin")
                .accept(MediaType.APPLICATION_JSON).param("username","admin1")
                .param("password","pass1234").param("authority","student")
                .param("email","18307110000@fudan.edu.cn"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Authority 'student' is not legal"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Transactional
    void register_adminHasRegistered() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/superAdmin/addAdmin")
                .accept(MediaType.APPLICATION_JSON).param("username","admin")
                .param("password","pass1234").param("authority","admin")
                .param("email","admin@fudan.edu.cn"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @Transactional
    void register_adminSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/superAdmin/addAdmin")
                .accept(MediaType.APPLICATION_JSON).param("username","admin1")
                .param("password","pass1234").param("authority","admin")
                .param("email","admin1@fudan.edu.cn"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }



    @Test
    void logout() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/logout"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}