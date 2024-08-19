package com.finalproject.sulbao.cart.dto;

import com.finalproject.sulbao.cart.domain.Order;
import com.finalproject.sulbao.cart.vo.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDTO {
    private Long orderCode;   // 주문코드
    private LocalDate orderDate; // 주문날짜
    private String delivery;  // 배송상태
    private boolean isReviewed; // 리뷰작성여부
    private boolean isPresent; // 선결제여부
    private String presentEmail; // 선물받는사람 이메일
    private String names; // 수신자 이름
    private String phoneNumber; // 수신자 핸드폰번호
    private String address1; // 주소1
    private String address2; // 주소2
    private String zipCode; // 우편번호
    private Set<OrderItemDTO> orderItems;
    private String userId;

    public Order toEntity() {
        // OrderItemDTO 리스트를 OrderItem 엔티티 리스트로 변환
        Set<OrderItem> orderItemEntities = orderItems.stream()
                .map(OrderItemDTO::toEntity)
                .collect(Collectors.toSet());

        // Order 엔티티 생성 및 반환
        return new Order(
                this.orderCode,
                this.orderDate,
                this.delivery,
                this.isReviewed,
                this.isPresent,
                this.presentEmail,
                this.names,
                this.phoneNumber,
                this.address1,
                this.address2,
                this.zipCode,
                orderItemEntities,
                this.userId
        );

    }
}
