package com.finalproject.sulbao.product.model.entity;

import com.finalproject.sulbao.common.entity.BaseEntity;
import com.finalproject.sulbao.login.model.entity.Login;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_review")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "review_content")
    private String reviewContent;

    @ManyToOne
    @JoinColumn(name = "product_no")
    private Product product;

    //등록한 유저정보
    @ManyToOne
    @JoinColumn(name = "user_no")
    private Login user;

    public void updateContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
