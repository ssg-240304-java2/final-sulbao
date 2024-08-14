package com.finalproject.sulbao.cart.dto;

import com.finalproject.sulbao.cart.vo.OrderItem;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderItemDTO {
    private Long productNo;
    private int amount;
    private int totalPrice;

    public OrderItem toEntity() {
            return new OrderItem(
                    this.productNo,
                    this.amount,
                    this.totalPrice
            );
        }
}
