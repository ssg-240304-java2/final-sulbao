package com.finalproject.sulbao.login;

import com.finalproject.sulbao.login.model.repository.LoginRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoginDDLTests {
    @Autowired
    public LoginRepository loginRepository;

    @AfterEach
    public void close(){
        loginRepository.deleteAll();
    }

    @Test
    @DisplayName("ddl test")
    public void test1(){


    }
}
