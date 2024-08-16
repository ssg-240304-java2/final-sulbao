package com.finalproject.sulbao.login.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfirmDto {
    private String confirmCode;
    private String businessEmail;
    private boolean isConfirm;
}
