package com.finalproject.sulbao.review.controller;

import com.finalproject.sulbao.cart.dto.OrderItemDTO;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.review.model.dto.ReviewDTO;
import com.finalproject.sulbao.review.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/orderList")
    public void orderList(Model model){}


    @GetMapping("/regist/{orderNo}/{productNo}")
    public String regist(@PathVariable String orderNo, @PathVariable String productNo, Model model){

        log.info("Controller orderNo = {}, productNo = {}", orderNo, productNo);

        //상품정보 취득
        ProductDTO product = reviewService.getProductInfo(productNo);
        log.info("검색 product = {}", product);

        //주문정보 취득
        OrderItemDTO orderItem = reviewService.getOrderInfo(orderNo,productNo);

        // 주문정보에 선택한 상품이 없는경우
        if(orderItem == null){
            return "redirect:/review/orderList";
        }

        model.addAttribute("product", product);
        model.addAttribute("orderItem", orderItem);
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("productNo", productNo);
        return "review/regist";
    }

    @PostMapping("/regist")
    public String regist(@ModelAttribute ReviewDTO reviewDTO, HttpSession session){
        log.info("Controller review = {}", reviewDTO);

        //로그인한 사용자 ID
        reviewDTO.setUserNo((Long) session.getAttribute("userNo"));
        reviewService.saveReview(reviewDTO);


        return "redirect:/review/orderList";
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
