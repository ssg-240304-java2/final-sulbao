package com.finalproject.sulbao.cart.controller;

import com.finalproject.sulbao.cart.dto.*;

import com.finalproject.sulbao.cart.service.CartService;
import com.finalproject.sulbao.cart.service.KakaoPayService;
import com.finalproject.sulbao.cart.service.OrderItemService;
import com.finalproject.sulbao.cart.service.OrderService;
import com.finalproject.sulbao.cart.session.SessionUtils;
import groovy.util.logging.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@Controller
@RequestMapping("/payments")
public class KakaoController {
    private static final Logger log = LoggerFactory.getLogger(KakaoController.class);
    private final KakaoPayService kakaoPayService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final CartService cartService;


    @Autowired
        public KakaoController(KakaoPayService kakaoPayService,OrderService orderService, OrderItemService orderItemService, CartService cartService) {
        this.kakaoPayService = kakaoPayService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.cartService = cartService;
    }

    @PostMapping("/kakaopay")
    public @ResponseBody ReadyResponse payReady(HttpServletRequest request,@RequestBody Map<String, Object> params) {
        HttpSession session = request.getSession();
        String name = (String) params.get("name");
        int totalPrice = Integer.parseInt((String) params.get("totalPrice"));
        String userId = (String) params.get("userId");


        List<String> orderCodeList = (List<String>) params.get("orderCode");
        int orderCode = Integer.parseInt(orderCodeList.get(0));
        System.out.println("orderCode = " + orderCode);

        int quantity = (int) params.get("quantity");
        String orderName = (String) params.get("orderName");
        String orderPhone = (String) params.get("orderPhone");
        String postcode = (String) params.get("postcode");
        String address = (String) params.get("address");
        String detailAddress = (String) params.get("detailAddress");
        log.info("params {}", params);
        System.out.println(orderCodeList);


        session.setAttribute("orderCodeList", orderCodeList);

        session.setAttribute("name", name);
        session.setAttribute("totalPrice", totalPrice);
        session.setAttribute("userId", userId);
        session.setAttribute("orderCode", orderCode);
        session.setAttribute("quantity", quantity);
        session.setAttribute("orderName", orderName);
        session.setAttribute("orderPhone", orderPhone);
        session.setAttribute("postcode", postcode);
        session.setAttribute("address", address);
        session.setAttribute("detailAddress", detailAddress);



        ReadyResponse readyResponse = kakaoPayService.payReady(name, totalPrice, userId, orderCode, quantity);
        SessionUtils.addAttribute("tid", readyResponse.getTid());
        log.info("결제 고유번호: {}", readyResponse.getTid());
        return readyResponse;
    }

    @GetMapping("/completed")
    public String payCompleted(@RequestParam("pg_token") String pgToken, HttpServletRequest request) {
        String tid = SessionUtils.getStringAttributeValue("tid");
        log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        log.info("결제 고유번호: " + tid);
        // 카카오 결제 요청하기
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        int orderCode = (int) session.getAttribute("orderCode");
        ApproveResponse approveResponse = kakaoPayService.payApprove(tid, pgToken, userId, orderCode);

        String delivery = "결제완료";
        boolean isReviewed = false;
        boolean isPresent = false;
        String names = (String) session.getAttribute("orderName");
        String phoneNumber = (String) session.getAttribute("orderPhone");
        String address1 = (String) session.getAttribute("address");
        String detailAddress = (String) session.getAttribute("detailAddress");
        String zipCode = (String) session.getAttribute("postcode");


        List<String> orderCodeList = (List<String>) session.getAttribute("orderCodeList");
        int len = orderCodeList.size();
        System.out.println("len = " + len);
        System.out.println("orderCodeList = " + orderCodeList);



        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setDelivery(delivery);
        orderDTO.setNames(names);
        orderDTO.setPhoneNumber(phoneNumber);
        orderDTO.setAddress1(address1);
        orderDTO.setReviewed(isReviewed);
        orderDTO.setPresent(isPresent);
        orderDTO.setAddress2(detailAddress);
        orderDTO.setZipCode(zipCode);

        for(int i=0; i<len; i++) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            CartDTO cartDTO = new CartDTO();
            cartDTO = cartService.findCartByCartCode(Long.valueOf(orderCodeList.get(i)));
            int amount = cartDTO.getAmount();
            int totalPrice = cartDTO.getTotalPrice();
            Long productNo = cartDTO.getProducts().getProductNo();
            System.out.println("productNo = " + productNo);
            orderItemDTO.setAmount(amount);
            orderItemDTO.setProductNo(productNo);
            orderItemDTO.setTotalPrice(totalPrice);
            orderDTO.setOrderItems(Set.of(orderItemDTO));

            Long orderPk = orderService.saveOrder(orderDTO);
            System.out.println(orderPk);

            cartService.updateIsOrder(cartDTO.getCartCode());

        }

        return "redirect:/ordercomplete";
    }
}
