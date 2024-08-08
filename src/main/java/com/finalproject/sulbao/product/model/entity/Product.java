package com.finalproject.sulbao.product.model.entity;

import com.finalproject.sulbao.common.entity.BaseEntity;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.product.model.vo.ProductImage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="tbl_product")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_no")
    private Long productNo;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_category")
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(name="product_description")
    private String productDescription;

    @Column(name="product_summary")
    private String productSummary;

    @Column(name="product_price")
    private int productPrice;

    @Column(name="product_stock")
    private int productStock;

    @Column(name="product_hashtag")
    private String productHashTag;

    @Column(name="sell_yn")
    private String sellYn;

    @Column(name="display_yn")
    private String displayYn;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name="tbl_product_image", joinColumns = @JoinColumn(name="product_no")
    )
    @OrderColumn(name="idx")
    private List<ProductImage> productImages;

    //판매자정보
    @ManyToOne
    @JoinColumn(name = "user_no")
    private Login sellerInfo;

}

