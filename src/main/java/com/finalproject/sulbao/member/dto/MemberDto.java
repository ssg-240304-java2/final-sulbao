package com.finalproject.sulbao.member.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDto {

    private Long userNo;
    private String userId;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private String businessName;
    private String businessNum;
    private String businessLink;
    private String role;
    private String status;
    private boolean isAblable;
    private String registDate;

    @Builder
    public MemberDto(Long userNo, String userId, String name, String gender, String email, String phone, String businessName, String businessNum, String businessLink, String role, String status, boolean isAblable, String registDate) {
        this.userNo = userNo;
        this.userId = userId;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.businessName = businessName;
        this.businessNum = businessNum;
        this.businessLink = businessLink;
        this.role = role;
        this.status = status;
        this.isAblable = isAblable;
        this.registDate = registDate;
    }
}
