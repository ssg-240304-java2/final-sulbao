package com.finalproject.sulbao.magazine.model.dto;

import com.finalproject.sulbao.product.model.entity.Magazine;
import com.finalproject.sulbao.product.model.vo.MagazineImage;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MagazineDTO {

    private Long magazineNo;
    private String magazineTitle;
    private String magazineSummary;
    private String magazineContent;
    private String magazineTag;
    private String publishDate;
    private String displayYn;
    private List<MultipartFile> magazineImages;
    private List<MagazineImage> magazineImagesList;

    public Magazine toEntity() {
        return Magazine.builder()
                .magazineTitle(magazineTitle)
                .magazineSummary(magazineSummary)
                .magazineContent(magazineContent)
                .magazineTag(magazineTag)
                .publishDate(LocalDate.parse(publishDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .displayYn(displayYn)
                .magazineImages(magazineImagesList)
                .build();
    }

    public MagazineDTO toDTO(Magazine magazine) {
        return MagazineDTO.builder()
                .magazineNo(magazine.getId())
                .magazineTitle(magazine.getMagazineTitle())
                .magazineSummary(magazine.getMagazineSummary())
                .magazineContent(magazine.getMagazineContent())
                .magazineTag(magazine.getMagazineTag())
                .publishDate(String.valueOf(magazine.getPublishDate()))
                .displayYn(magazine.getDisplayYn())
                .magazineImagesList(magazine.getMagazineImages())
                .build();
    }
}
