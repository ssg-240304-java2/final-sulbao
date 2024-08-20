package com.finalproject.sulbao.cart.controller;

import com.finalproject.sulbao.cart.dto.CartDTO;
import com.finalproject.sulbao.cart.dto.OrderDTO;
import com.finalproject.sulbao.cart.dto.OrderItemDTO;
import com.finalproject.sulbao.cart.service.CartService;
import com.finalproject.sulbao.cart.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }


    @GetMapping("/validateOrder")
    public String validateOrder(@RequestParam("token") String token, Model model, HttpServletRequest request) {
        // 토큰에 해당하는 orderCodeList를 조회
        HttpSession session = request.getSession();
        List<OrderDTO> orderCodeList = orderService.findByToken(token);
        List<CartDTO> cartLists = cartService.findCartByToken(token);
        if (orderCodeList != null) {
            int len = orderCodeList.size();

            session.setAttribute("len", len);

            model.addAttribute("orderCodeList", orderCodeList);
            session.setAttribute("token", orderCodeList.get(0).getToken());
            // orderCodeList를 사용해 필요한 작업 수행
            // 예: 주문 처리, 상태 업데이트 등
            // 작업 완료 후 토큰과 orderCodeList 매핑 삭제
            System.out.println("cartLists = " + cartLists);
            System.out.println("-------------------->>>>>>>>>>>>>>>>>>"+orderCodeList);
            model.addAttribute("carts", cartLists);

            return "success"; // 성공 페이지로 리다이렉션
        }
        else {
            return "error"; // 유효하지 않은 토큰 처리
        }
    }

    @PostMapping("/submitOrder")
    public String submitOrder(
        @RequestParam("orderName") String orderName,
        @RequestParam("orderPhone") String orderPhone,
        @RequestParam("postcode") String postcode,
        @RequestParam("address") String address,
        @RequestParam("detailAddress") String detailAddress,
        Model model, HttpServletRequest request) {
        System.out.println("야발");
        HttpSession session = request.getSession();

        String token = (String) session.getAttribute("token");
        try {
            orderService.updateOrderNameBytoken(orderName, token);
            orderService.updateOrderPhoneBytoken(orderPhone, token);
            orderService.updatePostCodeBytoken(postcode, token);
            orderService.updateAddress1Bytoken(address, token);
            orderService.updateAddress2Bytoken(detailAddress, token);
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }

        return "redirect:/presentcomplete"; // 주문 확인 페이지로 리다이렉트 또는 포워드
    }
}
