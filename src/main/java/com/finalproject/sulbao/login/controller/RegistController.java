package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.NewMemberDTO;
import com.finalproject.sulbao.login.model.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
@RequestMapping("/regist")
public class RegistController {

    private final LoginService loginService;

    public RegistController(LoginService loginService) {
        this.loginService = loginService;
    }


}
