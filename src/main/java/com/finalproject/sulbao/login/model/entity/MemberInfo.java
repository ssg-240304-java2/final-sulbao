package com.finalproject.sulbao.login.model.entity;

import com.finalproject.sulbao.login.model.vo.MemberImage;
import com.finalproject.sulbao.login.model.vo.ProMemberInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_member_info")
@SecondaryTables({
        @SecondaryTable(
                name = "tbl_pro_member_info",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "member_no", referencedColumnName = "member_no")
        ),
        @SecondaryTable(
                name = "tbl_member_image",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "member_no", referencedColumnName = "member_no")
        )
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    private Long memberNo;

    @OneToOne
    @JoinColumn(name = "user_no")
    private Login user;

    private LocalDateTime verifyageDate;

    private String profileImg;

    private String profileName;

    private String profileText;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "businessNumber", column = @Column(name = "business_number", table = "tbl_pro_member_info")),
            @AttributeOverride(name = "businessLink", column = @Column(name = "business_link", table = "tbl_pro_member_info")),
            @AttributeOverride(name = "proStatus", column = @Column(name = "pro_status", table = "tbl_pro_member_info")),
            @AttributeOverride(name = "proRegistDate", column = @Column(name = "pro_regist_date", table = "tbl_pro_member_info"))
    })
    private ProMemberInfo proMemberInfo;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "fileName", column = @Column(name = "file_name", table = "tbl_member_image")),
            @AttributeOverride(name = "saveName", column = @Column(name = "save_name", table = "tbl_member_image")),
            @AttributeOverride(name = "saveUrl", column = @Column(name = "save_img_url", table = "tbl_member_image")),
    })
    private MemberImage memberImage;

    @Builder
    public MemberInfo(Login user, String profileName, LocalDateTime verifyageDate) {
        this.user = user;
        this.profileName = profileName;
        this.profileImg = "default-profile.png";
        this.verifyageDate = LocalDateTime.now();

    }
}
