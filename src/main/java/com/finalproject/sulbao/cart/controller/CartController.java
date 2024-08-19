package com.finalproject.sulbao.cart.controller;

import com.finalproject.sulbao.cart.domain.Carts;
import com.finalproject.sulbao.cart.dto.CartDTO;
import com.finalproject.sulbao.cart.service.CartService;
import com.finalproject.sulbao.login.model.dto.LoginDetails;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/orders")
@Slf4j
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /***
     * 장바구니 로딩 및 카트 출력 메소드
     * @param model
     * @return
     */
    @GetMapping("/cart")
    public String viewCart(Model model,Authentication authentication, HttpSession session){
        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }
        LoginDetails login = (LoginDetails) authentication.getPrincipal();
        String userId = login.getUsername();
        List<CartDTO> cartList = cartService.findCartByUserId(userId);
        model.addAttribute("carts", cartList);
        return "cart";
    }

    /***
     * 장바구니 항목 삭제 메소드
     * @param cartCode
     * @return
     */
    @DeleteMapping("/cart/delete/{cartCode}")
    public ResponseEntity<String> deleteCart(@PathVariable Long cartCode){
        try{
            cartService.deleteByCartCode(cartCode);
            return ResponseEntity.ok("Product deleted successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product");
        }
    }

    /***
     * 장바구니 상품 수량 변경 메소드
     * @param cartCode
     * @param increment
     * @return
     */
    @PutMapping("/cart/updateQuantity/{cartCode}/{increment}")
    public ResponseEntity<Carts> updateQuantity(@PathVariable Long cartCode, @PathVariable boolean increment){
        try {
            Carts updatedCart = cartService.updateQuantity(cartCode, increment);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /***
     * 일반 주문 페이지로 이동 메소드
     * @param cartCodes
     * @return
     */
    @PostMapping("/cart/order_form")
    public String orderForm(@RequestParam("cartCodes") List<Long> cartCodes, Model model, HttpSession session) {
        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }

        if (cartCodes == null || cartCodes.isEmpty()) {
            throw new IllegalArgumentException("No products selected.");
        }
        session.setAttribute("cartCodes", cartCodes);
        System.out.println(cartCodes);
        List<CartDTO> checkOutList = cartService.findCartByCartCodeIn(cartCodes);
        int totalPurchasePrice = cartService.sumCartByCartCodeIn(cartCodes);
        model.addAttribute("carts", checkOutList);
        model.addAttribute("totalPurchasePrice", totalPurchasePrice);
        return "order";
    }


    /***
     * 선물하기 주문 페이지로 이동 메소드
     * @param cartCodes
     * @param model
     * @return
     */
    @PostMapping("/cart/present_form")
    public String presentForm(@RequestParam("cartCodes") List<Long> cartCodes, Model model, HttpSession session) {
        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }
        if (cartCodes == null || cartCodes.isEmpty()) {
            throw new IllegalArgumentException("No products selected.");
        }
        List<CartDTO> checkOutList = cartService.findCartByCartCodeIn(cartCodes);
        int totalPurchasePrice = cartService.sumCartByCartCodeIn(cartCodes);
        model.addAttribute("carts", checkOutList);
        model.addAttribute("totalPurchasePrice", totalPurchasePrice);
        return "presentOrder"; // View 또는 redirect 경로
    }
}