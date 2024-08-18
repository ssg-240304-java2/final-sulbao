package com.finalproject.sulbao.login.model.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MemberProfileDto {
    private String email;
    private String phone;
    private String profileImg;
    private String profileName;
    private String profileText;
}
