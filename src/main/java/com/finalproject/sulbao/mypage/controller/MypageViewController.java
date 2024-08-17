package com.finalproject.sulbao.mypage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageViewController {

    @GetMapping("/myprofile")
    public String myProfilePage() {
        return "mypage/myprofile";
    }
}
