package com.finalproject.sulbao.product.model.entity;

import com.finalproject.sulbao.common.entity.BaseEntity;
import com.finalproject.sulbao.magazine.model.dto.MagazineDTO;
import com.finalproject.sulbao.product.model.dto.ProductDTO;
import com.finalproject.sulbao.product.model.vo.MagazineImage;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_magazine")
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Magazine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "magazine_no")
    private Long id;

    @Column(name = "magazine_title")
    private String magazineTitle;

    @Column(name = "magazine_summary")
    private String magazineSummary;

    @Column(name = "magazine_content", columnDefinition = "TEXT")
    private String magazineContent;

    @Column(name = "magazine_tag")
    private String magazineTag;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @Column(name = "display_yn", length = 1, columnDefinition = "CHAR(1) DEFAULT 'n'")
    private String displayYn;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "tbl_magazine_image", joinColumns = @JoinColumn(name = "magazine_no")
    )
    @OrderColumn(name = "idx")
    private List<MagazineImage> magazineImages;

    public void update(MagazineDTO magazineDTO){

        this.id = magazineDTO.getMagazineNo();
        this.magazineTitle = magazineDTO.getMagazineTitle();
        this.magazineSummary = magazineDTO.getMagazineSummary();
        this.magazineContent = magazineDTO.getMagazineContent();
        this.magazineTag = magazineDTO.getMagazineTag();
        this.publishDate = LocalDate.parse(magazineDTO.getPublishDate());
        this.displayYn = magazineDTO.getDisplayYn();
        if(this.magazineImages != null){
            System.out.println("HIIIII");
            this.magazineImages = magazineDTO.getMagazineImagesList();
        }

    }
}
