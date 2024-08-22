package com.finalproject.sulbao.review.controller;

import com.finalproject.sulbao.cart.dto.OrderDTO;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.review.model.dto.ReviewDTO;
import com.finalproject.sulbao.review.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 주문 목록 화면 이동
    @GetMapping("/orderList")
    public void orderList(Model model){}


    // 리뷰 등록 페이지 이동
    @GetMapping("/regist/{orderNo}")
    public String regist(@PathVariable String orderNo, Model model){

        //주문정보 취득
        OrderDTO orderDTO = reviewService.getOrderInfo(orderNo);

        //상품정보 취득
        ProductDTO product = reviewService.getProductInfo(orderDTO.getOrderItems());

        model.addAttribute("order", orderDTO);
        model.addAttribute("product", product);
        return "review/regist";
    }

    // 리뷰 등록
    @PostMapping("/regist")
    public String regist(@ModelAttribute ReviewDTO reviewDTO, HttpSession session){

        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }

        //로그인한 사용자 ID
        reviewDTO.setUserNo((Long) session.getAttribute("userNo"));
        reviewService.saveReview(reviewDTO);

        return "redirect:/review/orderList";
    }

    // 상세조회(리뷰목록에서)
    @GetMapping("/detail/{reviewId}")
    public String detail(@PathVariable Long reviewId, Model model){

        ReviewDTO reviewDTO = reviewService.findById(reviewId);

        model.addAttribute("review", reviewDTO);
        return "review/detail";
    }

    // 상품 리뷰 수정(상세페이지에서)
    @GetMapping("/update/{reviewId}")
    public String updateReview(@PathVariable Long reviewId,Model model){
        ReviewDTO reviewDTO = reviewService.findById(reviewId);
        model.addAttribute("review", reviewDTO);
        return "review/update";
    }

    // 등록한 리뷰 목록
    @GetMapping("/list")
    public String list(HttpSession session, Model model){

        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }

        List<ReviewDTO> reviewList = reviewService.findByUserNo((Long) session.getAttribute("userNo"));
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("menu","review");
        return "review/list";
    }

    // 등록한 리뷰 수정
    @PostMapping("/update")
    public String update(@ModelAttribute ReviewDTO reviewDTO, HttpSession session){

        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }

        reviewService.updateReview(reviewDTO);

        return "redirect:/review/list";

    }

    //리뷰 삭제
    @DeleteMapping("/delete/{reviewId}")
    @ResponseBody
    public String delete(@PathVariable Long reviewId){
//        if(session.getAttribute("userNo") == null){
//            return "redirect:/login";
//        }
        reviewService.deleteReview(reviewId);
        return "success";
    }
}
