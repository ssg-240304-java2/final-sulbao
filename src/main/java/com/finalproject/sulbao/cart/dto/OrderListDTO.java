package com.finalproject.sulbao.cart.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderListDTO {
    private Long orderCode;
    private String picture;
    private String company;
    private String name;
    private int amount;
    private int totalPirce;
    private String status;
    private LocalDate orderDate;
    private boolean isReviewed;
}
