package com.finalproject.sulbao.login.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewMemberDTO {

    private String userId;
    private String userPw;
    private String gender;

}
