package com.finalproject.sulbao.review.model.dto;

import lombok.*;

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

}
