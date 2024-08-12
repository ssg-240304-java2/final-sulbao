package com.finalproject.sulbao.cart.domain;

import com.finalproject.sulbao.login.model.entity.Login;
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
    private Long userId;

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

    public void increaseAmount() {
        if(this.amount < 99){
            this.amount += 1;
            updateTotalPrice();
        }
    }
    public void decreaseAmount() {
        if(this.amount > 1) {
            this.amount -= 1;
            updateTotalPrice();
        }
    }

    public void updateTotalPrice() {
        this.totalPrice = this.amount * this.products.getProductPrice();
    }
}
