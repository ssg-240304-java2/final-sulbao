package com.finalproject.sulbao.product.model.dto;

import com.finalproject.sulbao.common.entity.BaseEntity;
import com.finalproject.sulbao.product.model.vo.ComparisonImage;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductComparisonDTO extends BaseEntity {

    private Long comparisonNo;

    private String comparisonName;

    private String comparisonDescription;

    private String comparisonCategory;

    private Integer minPrice;

    private List<ComparisonImage> productImages;

    private List<ProductDTO> shoppingMallInfo;

}
