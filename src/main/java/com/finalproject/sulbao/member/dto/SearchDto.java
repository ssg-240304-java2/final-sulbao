package com.finalproject.sulbao.member.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchDto {

    private String type;
    private String search;
    private String statusAll;
    private String roleCategory;
    private String availableYn;
}
