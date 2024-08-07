package com.finalproject.sulbao.login.model.entity;

import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.StatusType;
import com.finalproject.sulbao.login.model.vo.ProMemberInfo;
import com.finalproject.sulbao.login.model.vo.SellerInfo;
import jakarta.persistence.*;
import lombok.*;
import org.apache.ibatis.annotations.One;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_member_info")
@SecondaryTables({
        @SecondaryTable(
                name = "tbl_pro_member_info",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "member_no", referencedColumnName = "member_no")
        )
})
@NoArgsConstructor
@AllArgsConstructor
@Data
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
            @AttributeOverride(name="businessNumber", column = @Column(name="business_number", table = "tbl_pro_member_info")),
            @AttributeOverride(name="businessLink", column = @Column(name="business_link", table = "tbl_pro_member_info")),
            @AttributeOverride(name="proStatus", column = @Column(name="pro_status", table = "tbl_pro_member_info")),
            @AttributeOverride(name="proRegistDate", column = @Column(name="pro_regist_date", table = "tbl_pro_member_info"))
    })
    private ProMemberInfo proMemberInfo;




}
