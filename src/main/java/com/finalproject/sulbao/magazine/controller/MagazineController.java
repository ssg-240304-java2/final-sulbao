package com.finalproject.sulbao.magazine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/magazine")
public class MagazineController {

    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("menu", "magazine");
        model.addAttribute("submenu", "list");
        return "adm/magazine/list";
    }

    @GetMapping("/detail")
    public String detail(Model model){
        model.addAttribute("menu", "magazine");
        model.addAttribute("submenu", "detail");
        return "adm/magazine/detail";
    }
}
