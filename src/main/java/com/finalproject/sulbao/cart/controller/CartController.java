package com.finalproject.sulbao.cart.controller;

import com.finalproject.sulbao.cart.domain.Carts;
import com.finalproject.sulbao.cart.dto.CartDTO;
import com.finalproject.sulbao.cart.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * 카트 출력 메소드
     * @param model
     * @return
     */
    @GetMapping("/cart")
    public String viewCart(Model model){
        Long userId = 1L;

        List<CartDTO> cartList = cartService.findCartByUserId(userId);
        System.out.println("lalalallalalal"+ cartService.findCartByUserId(userId));
        System.out.println(cartList);
        log.info("cartList =깐트롤라 {}", cartList);
        model.addAttribute("carts", cartList);
        return "cart";
    }

    /***
     * 카트 내용물 삭제 메소드
     * @param cartCode
     * @return
     */
    @DeleteMapping("/cart/delete/{cartCode}")
    public ResponseEntity<String> deleteCart(@PathVariable Long cartCode){
        log.info("dldlldldldldl {}", cartCode);
        try{
            cartService.deleteByCartCode(cartCode);
            return ResponseEntity.ok("Product deleted successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting product");
        }
    }

    @PutMapping("/cart/updateQuantity/{cartCode}/{increment}")
    public ResponseEntity<Carts> updateQuantity(@PathVariable Long cartCode, @PathVariable boolean increment){
        try {
            Carts updatedCart = cartService.updateQuantity(cartCode, increment);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/cart/order_form")
    public String orderForm(@RequestParam(value = "cartNos", required = false) List<Long> cartNos) {
        if (cartNos == null || cartNos.isEmpty()) {
            throw new IllegalArgumentException("No products selected.");
        }
        // cartNos 리스트를 사용하여 로직 처리
        return "orderConfirmation";
    }

    @PostMapping("/cart/present_form")
    public String presentForm(@RequestParam(value = "cartNos", required = false) List<Long> cartNos) {
        if (cartNos == null || cartNos.isEmpty()) {
            throw new IllegalArgumentException("No products selected.");
        }
        // cartNos 리스트를 사용하여 로직 처리
        return "presentConfirmation";
    }
}