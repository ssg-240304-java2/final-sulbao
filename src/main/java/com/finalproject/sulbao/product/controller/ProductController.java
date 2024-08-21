package com.finalproject.sulbao.product.controller;

import com.finalproject.sulbao.product.model.dto.ProductComparisonDTO;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.entity.ProductCategory;
import com.finalproject.sulbao.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //목록화면
    @GetMapping("/list")
    public String productList(Model model, HttpSession session) {

        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setUserNo((Long) session.getAttribute("userNo"));
        List<ProductDTO> productList = productService.findByUserNo(productDTO);

        model.addAttribute("menu","product");
        model.addAttribute("submenu","list");
        model.addAttribute("product", productDTO);
        model.addAttribute("productList", productList);
        return "admin/product/productList";
    }

    // 관리자 페이지 조회
    @GetMapping("/search")
    public String productSearch(Model model, @ModelAttribute ProductDTO productDTO, HttpSession session) {

        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }

        productDTO.setUserNo((Long) session.getAttribute("userNo"));
        List<ProductDTO> productList = productService.findBySearchInfo(productDTO);
        model.addAttribute("menu","product");
        model.addAttribute("submenu","list");
        model.addAttribute("product", productDTO);
        model.addAttribute("productList", productList);
        return "admin/product/productList";
    }

    //등록화면으로 이동
    @GetMapping("/detail")
    public String productRegist(Model model, HttpSession session) {

        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }

        List<ProductComparisonDTO> productComparisonList = productService.findByComparison();
        model.addAttribute("menu","product");
        model.addAttribute("submenu","regist");
        model.addAttribute("productComparisonList", productComparisonList);
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("productCategory", ProductCategory.values());
        return "admin/product/productDetail";
    }

    //수정화면으로 이동
    @GetMapping("/update/{productNo}")
    public String productUpdate(Model model,@PathVariable(required = false,name = "productNo") Long productNo, HttpSession session) {

        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }

        List<ProductComparisonDTO> productComparisonList = productService.findByComparison();
        model.addAttribute("productComparisonList", productComparisonList);

        ProductDTO productDTO =  productService.findByProductNo(productNo);
        model.addAttribute("product", productDTO);
        model.addAttribute("productCategory", ProductCategory.values());
        return "admin/product/productUpdate";
    }

    //상품등록
    @PostMapping("/regist")
    public String saveProduct(@ModelAttribute ProductDTO productDTO, HttpSession session) {

        log.info("productController saveProduct : {}", productDTO);
        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }
        productDTO.setUserNo((Long) session.getAttribute("userNo"));
        // 저장
        productService.saveProduct(productDTO);
        return "redirect:/product/list";
    }

    //상품수정
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute ProductDTO productDTO, HttpSession session) {

        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }
        productDTO.setUserNo((Long) session.getAttribute("userNo"));
        productService.updateProduct(productDTO);

        return "redirect:/product/list";
    }

    //상품삭제
    @DeleteMapping("/delete")
    @ResponseBody
    public String deleteProduct(String productNoList, HttpSession session) {
        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }
        productService.delete(productNoList);
        return "success";
    }

    //상품상태변경
    @PutMapping("/status")
    @ResponseBody
    public String updateStatus(String productNoList, String type, String status, HttpSession session) {
        if(session.getAttribute("userNo") == null){
            return "redirect:/login";
        }
        if(productNoList == null || productNoList.isEmpty()){
            return "fail";
        }
        productService.updateStatus(productNoList,type,status);
        return "success";
    }

    // 사용자 페이지 상품목록
    @GetMapping("/user/list")
    public String userList(Model model) {
        return "product/list";
    }

    // 사용자 페이지 상품상세조회
    @GetMapping("/user/detail")
    public String userDetail(Model model) {
        return "product/detail";
    }

}
