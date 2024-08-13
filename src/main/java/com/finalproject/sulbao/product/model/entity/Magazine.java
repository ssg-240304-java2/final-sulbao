package com.finalproject.sulbao.product.model.entity;

import com.finalproject.sulbao.common.entity.BaseEntity;
import com.finalproject.sulbao.product.model.vo.MagazineImage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_magazine")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Magazine  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "magazine_no")
    private Long id;

    @Column(name = "magazine_title")
    private String magazineTitle;

    @Column(name = "magazine_category")
    private int magazineCategory;

    @Column(name = "publish_date")
    private LocalDateTime publishDate;

    @Column(name = "magazine_summary")
    private String magazineSummary;

    @Column(name = "magazine_content")
    private String magazineContent;

    @Column(name = "display_yn")
    private String displayYn;

    @Column(name = "magazine_tag")
    private String magazineTage;

    @Column(name = "magazine_template")
    private int magazineTemplate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_magazine_image", joinColumns = @JoinColumn(name = "magazine_no")
    )
    @OrderColumn(name = "idx")
    private List<MagazineImage> magazineImages;
}
