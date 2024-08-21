package com.finalproject.sulbao.login.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProFormDto {

    String userId;
    String businessEmail;
    String businessEmailCode;
    String businessNumber;
    String businessLink;
}
