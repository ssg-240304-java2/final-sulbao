package com.finalproject.sulbao.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class LoginController {

    @GetMapping("/login")
    public void loginPage(){}

    @GetMapping("/signup")
    public void signUpPage(){}



}


