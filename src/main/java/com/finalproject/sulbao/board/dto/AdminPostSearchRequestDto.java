package com.finalproject.sulbao.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminPostSearchRequestDto {

    private String type;
    private String keyword;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;

}
