package com.finalproject.sulbao.auth.controller;

import com.amazonaws.services.applicationinsights.model.LogFilter;
import com.finalproject.sulbao.auth.model.dto.OrderStatus;
import com.finalproject.sulbao.auth.service.AuthService;
import com.finalproject.sulbao.board.common.SessionHandler;
import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.service.PostService;
import com.finalproject.sulbao.login.model.dto.LoginDetails;
import com.finalproject.sulbao.product.model.dto.ProductComparisonDTO;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import com.finalproject.sulbao.board.dto.PostDto;
import com.finalproject.sulbao.board.service.PostService;
import com.finalproject.sulbao.product.model.dto.ProductComparisonDTO;
import com.finalproject.sulbao.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.List;

import static com.finalproject.sulbao.board.common.BoardCategoryConstants.ZZANFEED_ID;
import static com.finalproject.sulbao.board.common.BoardCategoryConstants.ZZANPOST_ID;

@Controller
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final SessionHandler sessionHandler;
    private final ProductService productService;
    private final PostService postService;

    public AuthController(AuthService authService, SessionHandler sessionHandler, ProductService productService, PostService postService) {
        this.authService = authService;
        this.sessionHandler = sessionHandler;
        this.productService = productService;
        this.postService = postService;
    }


    @GetMapping("/main")
    public String adminMain(Model model, Authentication authentication) {
        if(!sessionHandler.isLogin(authentication)){
            return "redirect:/login";
        }
        LoginDetails login = (LoginDetails) authentication.getPrincipal();

        String role = login.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        if(role.equals("MEMBER")){
            return "redirect:/";
        }

        //메뉴 고정
        model.addAttribute("menu","home");

        // 결제완료 조회

        List<OrderStatus> statusList = new ArrayList<>();
        Long userNo = login.getUserNo();
        if("ADMIN".equals(role)) {
            userNo = null;
        }

        OrderStatus paymentCompleted = authService.OrderStatusCountList(userNo,"결제완료");
        OrderStatus deliveryCompleted = authService.OrderStatusCountList(userNo,"배송완료");
        OrderStatus refund = authService.OrderStatusCountList(userNo,"환불중");
        OrderStatus purchaseCompleted = authService.OrderStatusCountList(userNo,"구매확정");

        statusList.add(paymentCompleted);
        statusList.add(deliveryCompleted);
        statusList.add(refund);
        statusList.add(purchaseCompleted);

        model.addAttribute("statusList", statusList);

        return "admin/index";
    }

    @GetMapping(value = {"/","/index"})
    public String index(Model model) {

        List<ProductComparisonDTO> productList = productService.findByProductComparsionOrderByDesc();
        model.addAttribute("productList", productList);

        List<PostDto> zzanfeeds = postService.getWeeklyposts(ZZANFEED_ID);
        List<PostDto> zzanposts = postService.getWeeklyposts(ZZANPOST_ID);
        model.addAttribute("zzanfeeds", zzanfeeds);
        model.addAttribute("zzanposts", zzanposts);

        return "index";
    }

}
