package com.finalproject.sulbao.product.model.dto;

import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.product.model.entity.Product;
import com.finalproject.sulbao.product.model.entity.ProductCategory;
import com.finalproject.sulbao.product.model.vo.ProductImage;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductDTO {

    private Long productNo;
    private String displayYn = "N";
    private String sellYn = "N";
    private ProductCategory productCategory;
    private String productName;
    private String productSummary;
    private String productDescription;
    private String productHashTag;
    private int productPrice;
    private int productStock;
    private MultipartFile image;
    private List<ProductImage> productImages = new ArrayList<>();
    private String searchType;
    private String searchInput;
    private Long userNo;
    private String searchchk;
    private Login sellerInfo;


    public Product toEntity() {
        return Product.builder()
                .productNo(productNo)
                .productName(productName)
                .productCategory(productCategory)
                .productDescription(productDescription)
                .productSummary(productSummary)
                .productPrice(productPrice)
                .productStock(productStock)
                .productHashTag(productHashTag)
                .sellYn(sellYn)
                .sellerInfo(sellerInfo)
                .displayYn(displayYn)
                .productImages(productImages)
                .build();
    }

    public ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .productNo(product.getProductNo())
                .productName(product.getProductName())
                .productCategory(product.getProductCategory())
                .productDescription(product.getProductDescription())
                .productSummary(product.getProductSummary())
                .productPrice(product.getProductPrice())
                .productStock(product.getProductStock())
                .productHashTag(product.getProductHashTag())
                .sellYn(product.getSellYn())
                .sellerInfo(product.getSellerInfo())
                .displayYn(product.getDisplayYn())
                .productImages(product.getProductImages())
                .build();
    }

}
