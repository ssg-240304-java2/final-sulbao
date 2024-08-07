package com.finalproject.sulbao.cart.domain;

import com.finalproject.sulbao.product.model.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "tbl_cart")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_code")
    private Long cartCode;

    @Column(name = "user_id")
    private String userId;

    //    @ElementCollection(fetch = FetchType.LAZY)
//    @CollectionTable(
//            name = "tbl_product", joinColumns = @JoinColumn(name = "product_no")
//    )
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_no")
    private Product products;

    @Column(nullable = false)
    private int amount;
    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isOrder;
}
