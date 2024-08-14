package com.finalproject.sulbao.product.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MagazineImage {

    private String originName;
    private String saveName;
    private String saveImgUrl;
}
