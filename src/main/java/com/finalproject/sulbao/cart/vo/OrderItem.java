package com.finalproject.sulbao.cart.vo;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
//        private Long code;
        // 상품번호
        private Long productNo;
        // 수량
        private int amount;
        // 총가격
        private int totalPrice;

}
