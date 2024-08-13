package com.finalproject.sulbao.product.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/list")
    public String productList() {
        return "product/productList";
    }

    @GetMapping("/detail")
    public String productDetail() {
        return "product/productDetail";
    }

//    @PostMapping("/info")
//    public String saveProduct(@ModelAttribute ProductDTO productDTO) {
//        //service 연결
//        return "redirect:/product/list";
//    }

}
