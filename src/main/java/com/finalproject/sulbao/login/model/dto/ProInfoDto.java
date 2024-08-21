package com.finalproject.sulbao.login.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProInfoDto {

    private Long memberNo;
    private String businessNumber;
    private String businessLink;
    private String proStatus;
}
