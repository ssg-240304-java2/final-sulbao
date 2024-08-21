package com.finalproject.sulbao.magazine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MagazineController {

    @GetMapping("/magazine/list")
    public String list(Model model){
        model.addAttribute("menu", "magazine");
        model.addAttribute("submenu", "list");
        return "admin/magazine/list";
    }

    @GetMapping("/magazine/detail")
    public String detail(Model model){
        model.addAttribute("menu", "magazine");
        model.addAttribute("submenu", "detail");
        return "admin/magazine/detail";
    }

    @GetMapping("/magazine")
    public String magazineList(Model model){
        return "magazine/list";
    }
}
