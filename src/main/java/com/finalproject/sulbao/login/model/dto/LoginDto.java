package com.finalproject.sulbao.login.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Valid
public class LoginDto {

    @NotBlank
    private String userId;
    @NotBlank
    private String userPw;
    private String userRole;

}
