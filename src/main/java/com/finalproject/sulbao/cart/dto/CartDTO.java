package com.finalproject.sulbao.cart.dto;

import com.finalproject.sulbao.product.model.entity.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartDTO {
    private Long cartCode;
    private String userId;
    private Product products;
    private int amount;
    private int totalPrice;
    private boolean isOrder;
    private String token;
}

