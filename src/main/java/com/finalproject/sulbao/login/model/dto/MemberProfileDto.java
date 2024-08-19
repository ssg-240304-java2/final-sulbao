package com.finalproject.sulbao.login.model.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
public class MemberProfileDto {
    private String profileImg;
    private String profileName;

    private String profileText;
    private String email;
    private String birth;
    private String phone;
    private String gender;
}
