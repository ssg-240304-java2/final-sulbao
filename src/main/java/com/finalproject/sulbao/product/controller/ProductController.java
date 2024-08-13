package com.finalproject.sulbao.product.controller;

import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.entity.ProductCategory;
import com.finalproject.sulbao.product.service.ProductService;
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
    public String productList(Model model) {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setUserNo(2L);
        List<ProductDTO> productList = productService.findByUserNo(productDTO);

        model.addAttribute("product", productDTO);
        model.addAttribute("productList", productList);
        return "product/productList";
    }

    //조회
    @GetMapping("/search")
    public String productSearch(Model model, @ModelAttribute ProductDTO productDTO) {

        productDTO.setUserNo(2L);
        List<ProductDTO> productList = productService.findBySearchInfo(productDTO);

        model.addAttribute("product", productDTO);
        model.addAttribute("productList", productList);
        return "product/productList";
    }

    //등록화면으로 이동
    @GetMapping("/detail")
    public String productRegist(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("productCategory", ProductCategory.values());
        return "product/productDetail";
    }

    //수정화면으로 이동
    @GetMapping("/update/{productNo}")
    public String productUpdate(Model model,@PathVariable(required = false,name = "productNo") Long productNo) {

        ProductDTO productDTO =  productService.findByProductNo(productNo);
        model.addAttribute("product", productDTO);
        model.addAttribute("productCategory", ProductCategory.values());
        return "product/productUpdate";
    }

    //상품등록
    @PostMapping("/regist")
    public String saveProduct(@ModelAttribute ProductDTO productDTO) {
        // 넘겨오는 값 확인
        log.info("saveProduct : {}", productDTO);
        // 저장
        productService.saveProduct(productDTO);
        return "redirect:/product/list";
    }

    //상품수정
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute ProductDTO productDTO) {

        productService.updateProduct(productDTO);

        return "redirect:/product/list";
    }

    //상품삭제
    @DeleteMapping("/delete")
    @ResponseBody
    public String deleteProduct(String productNoList) {
        productService.delete(productNoList);
        return "success";
    }

    //상품상태변경
    @PutMapping("/status")
    @ResponseBody
    public String updateStatus(String productNoList, String type, String status) {
        productService.updateStatus(productNoList,type,status);
        return "success";
    }

}
