package com.finalproject.sulbao.cart.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderProductDTO {
    private Long Code;
    private String name;
    private String orderName;
    private int quantity;
    private String productStatus;
    private int totalPrice;
    private String present;
}
