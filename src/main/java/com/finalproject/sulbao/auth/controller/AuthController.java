package com.finalproject.sulbao.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/main")
    public String adminMain(Model model) {
        model.addAttribute("menu","home");
        return "admin/index";
    }


}
