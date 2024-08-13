package com.finalproject.sulbao.login.model.entity;

import com.finalproject.sulbao.common.entity.BaseEntity;
import com.finalproject.sulbao.login.model.vo.SellerInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_login")
@SecondaryTables({
        @SecondaryTable(
                name = "tbl_seller_info",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "seller_no", referencedColumnName = "user_no")
        ),
        @SecondaryTable(
                name = "tbl_verify_code",
                pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_no", referencedColumnName = "user_no")
        )
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
public class Login extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType userRole;

    private String name;

    private String birth;

    private String gender;

    private String phone;

    private String email;

    /* 객체 탐색을 위해 추가 */
    @OneToOne(mappedBy = "user")
    private MemberInfo memberInfo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "businessNumber", column = @Column(name = "business_number", table = "tbl_seller_info")),
            @AttributeOverride(name = "businessName", column = @Column(name = "business_name", table = "tbl_seller_info")),
            @AttributeOverride(name = "sellerStatus", column = @Column(name = "seller_status", table = "tbl_seller_info"))
    })
    private SellerInfo sellerInfo;

//    @Builder
//    public Login(String userId, String userPw, RoleType userRole, String gender) {
//        this.userId = userId;
//        this.userPw = userPw;
//        this.userRole = userRole;
//        this.gender = gender;
//    }

    @Builder
    public Login(String userId, String userPw, RoleType userRole, String gender, String phone, String email, SellerInfo sellerInfo) {
        this.userId = userId;
        this.userPw = userPw;
        this.userRole = userRole;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.sellerInfo = sellerInfo;
    }
}