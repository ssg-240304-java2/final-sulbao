package com.finalproject.sulbao.product.model.vo;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    private String originName;
    private String saveName;

}