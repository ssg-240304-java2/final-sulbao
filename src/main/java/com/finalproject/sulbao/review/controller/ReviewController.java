package com.finalproject.sulbao.review.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/review")
public class ReviewController {

    @GetMapping("/orderList")
    public void orderList(Model model){}

    @GetMapping("/regist")
    public String regist(){
        return "review/regist";
    }

    @GetMapping("/detail")
    public String detail(){
        return "review/detail";
    }

    @GetMapping("/list")
    public String list(){
        return "review/list";
    }
}
