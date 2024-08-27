package com.finalproject.sulbao.auth.controller;

import com.amazonaws.services.applicationinsights.model.LogFilter;
import com.finalproject.sulbao.auth.model.dto.OrderStatus;
import com.finalproject.sulbao.auth.service.AuthService;
import com.finalproject.sulbao.board.common.SessionHandler;
import com.finalproject.sulbao.login.model.dto.LoginDetails;
import com.finalproject.sulbao.product.model.dto.ProductComparisonDTO;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final SessionHandler sessionHandler;
    private final ProductService productService;

    public AuthController(AuthService authService, SessionHandler sessionHandler, ProductService productService) {
        this.authService = authService;
        this.sessionHandler = sessionHandler;
        this.productService = productService;
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

        return "index";
    }

}
