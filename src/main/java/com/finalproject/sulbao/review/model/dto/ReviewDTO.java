package com.finalproject.sulbao.review.model.dto;

import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReviewDTO {

    private Long reviewId;
    private Long productNo;
    private Long userNo;
    private Long orderNo;
    private String reviewContent;
    private ProductDTO productDTO;
    private Login SellerInfo;
    private Login user;
    private LocalDateTime createDate;


}
