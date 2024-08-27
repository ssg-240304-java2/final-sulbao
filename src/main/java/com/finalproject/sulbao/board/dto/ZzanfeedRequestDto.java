package com.finalproject.sulbao.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class ZzanfeedRequestDto {

    private String title;
    private String tags;
    private MultipartFile thumbnail;
    private List<MultipartFile> contentImages;
    private List<String> contents;

}
