package com.finalproject.sulbao.cart.vo;

import com.finalproject.sulbao.common.entity.BaseEntity;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {
//        private Long code;
        // 상품번호
        private Long productNo;
        // 수량
        private int amount;
        // 총가격
        private int totalPrice;

}
