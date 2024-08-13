package com.finalproject.sulbao.cart.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReadyResponse {
    private String tid; // 결제 고유 번호
    private String next_redirect_pc_url; // 요청한 클라이언트가 pc웹
}
