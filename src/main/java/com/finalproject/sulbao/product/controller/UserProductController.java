package com.finalproject.sulbao.product.controller;

import com.finalproject.sulbao.cart.dto.CartDTO;
import com.finalproject.sulbao.login.model.dto.LoginDetails;
import com.finalproject.sulbao.product.model.dto.ProductComparisonDTO;
import com.finalproject.sulbao.cart.service.CartService;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.entity.Product;
import com.finalproject.sulbao.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
@Slf4j
public class UserProductController {

    private final ProductService productService;
    private final CartService cartService;

    public UserProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }


    // 사용자 페이지 상품목록
    @GetMapping("/user/list")
    public String userList(Model model) {

        //상품 최저가 구분 상품 정보 취득
        List<ProductComparisonDTO> comparisonList =  productService.findByComparisonList();

        // 쇼핑몰 정보 취득
        for (ProductComparisonDTO comparison : comparisonList){
            List<ProductDTO> productList = productService.findByProductPriceToComparisonNo(comparison.getComparisonNo());
            comparison.setShoppingMallInfo(productList);
        }

        model.addAttribute("comparisonList", comparisonList);
        return "product/list";
    }

    // 사용자 페이지 상품최저가
    @GetMapping("/user/low/{comparisonNo}")
    public String userDetail(@PathVariable Long comparisonNo ,Model model) {
        log.info("comparisonNo ===== {}", comparisonNo);


        //최저가 비교 상품 구분
        ProductComparisonDTO comparison = productService.findByComparisonNo(comparisonNo);
        log.info("최저가 비교 상품 구분 ================== {}",comparison);

        // 쇼핑몰 정보
        List<ProductDTO> productList = productService.findByProductPriceToComparisonNo(comparison.getComparisonNo());
        log.info("쇼핑몰 정보 리스트 ================== {}",productList);

        model.addAttribute("comparison", comparison);
        model.addAttribute("productList", productList);

        return "product/low";
    }

    //사용자 페이지 상품 상세
    @GetMapping("/user/detail/{productNo}")
    public String detail(@PathVariable Long productNo, Model model, HttpSession session) {
        ProductDTO productDTO = productService.findByProductNo(productNo);
        log.info("productDTO: {}", productDTO);
        model.addAttribute("product", productDTO);
        model.addAttribute("isLogin", session.getAttribute("userNo") != null);
        return "product/detail";
    }

    //장바구니 추가
    @PostMapping("/addCart")
    @ResponseBody
    public String addCart(@RequestParam Long productNo, @RequestParam Integer quantity, @RequestParam Integer totalPrice, Authentication authentication) {

        log.info("authentication: {}", authentication);
        log.info("productNo: {}", productNo);
        log.info("quantity: {}", quantity);
        log.info("totalPrice: {}", totalPrice);

        if(authentication == null) {
            return "redirect:/login";
        }

        String userId = ((LoginDetails) authentication.getPrincipal()).getUsername();
        CartDTO cartDTO = new CartDTO();
        cartDTO.setUserId("sulbao");
        cartDTO.setUserId(userId);
        cartDTO.setProducts(new Product().builder().productNo(productNo).build());
        cartDTO.setTotalPrice(totalPrice);
        cartDTO.setAmount(quantity);
        List<CartDTO> cartDTOList = cartService.findCartByProductNo(productNo, userId);

        System.out.println("테스트 ---------------->" + cartDTOList);
        // 여기서 카트 조회 한 후productno값이 같을 경우 가격 수정 , 수량 수정
        if(cartDTOList.size() == 1){
            if(quantity + cartDTOList.get(0).getAmount() > 99){
                return "amountError";
            }else {
                cartService.updateQuantityByCartNo(cartDTOList.get(0).getCartCode(), quantity + cartDTOList.get(0).getAmount());
                cartService.updateTotalPriceByCartNo(cartDTOList.get(0).getCartCode(), totalPrice + cartDTOList.get(0).getTotalPrice());
            }
        }else{
            productService.addCart(cartDTO);
        }
        return "success";
    }

}
