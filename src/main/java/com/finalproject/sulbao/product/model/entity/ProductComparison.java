package com.finalproject.sulbao.product.model.entity;

import com.finalproject.sulbao.common.entity.BaseEntity;
import com.finalproject.sulbao.product.model.vo.ComparisonImage;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="tbl_comparison")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductComparison extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comparison_no")
    private Long comparisonNo;

    @Column(name = "comparison_name")
    private String comparisonName;

    @Column(name = "comparison_description", columnDefinition = "LONGTEXT" )
    private String comparisonDescription;

    @Column(name = "comparison_category")
    private String comparisonCategory;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="tbl_comparison_image", joinColumns = @JoinColumn(name="comparison_no")
    )
    @OrderColumn(name="idx")
    private List<ComparisonImage> comparisonImages;

}
