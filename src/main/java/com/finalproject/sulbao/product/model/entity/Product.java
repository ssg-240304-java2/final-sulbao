package com.finalproject.sulbao.product.model.entity;

import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.product.model.vo.ProductImage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_product")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_no")
    private Long productNo;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @Column(name = "product_content")
    private String productContent;

    @Column(name = "product_summary")
    private String productSummary;

    @Column(name = "product_price")
    private int productPrice;

    @Column(name = "product_brand")
    private String productBrand;

    @Column(name = "product_stock")
    private String productStock;

    @Column(name = "product_hashtag")
    private String productHashtag;

    @Column(name = "sell_yn")
    private String sellYn;

    @Column(name = "display_yn")
    private String displayYn;

    @Column(name = "create_at")
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_date")
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_product_image", joinColumns = @JoinColumn(name = "product_no")
    )
    @OrderColumn(name = "idx")
    private List<ProductImage> productImages;

    //판매자정보
    @OneToOne
    @JoinColumn(name = "user_no")
    private Login sellerInfo;
}

