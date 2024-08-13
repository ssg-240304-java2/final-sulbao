package com.finalproject.sulbao.login.model.entity;

import com.finalproject.sulbao.common.entity.BaseEntity;
import com.finalproject.sulbao.login.model.vo.EmailVerify;
import com.finalproject.sulbao.login.model.vo.SellerInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "verifyCode", column = @Column(name = "verify_code", table = "tbl_verify_code")),
            @AttributeOverride(name = "isVerified", column = @Column(name = "is_verified", table = "tbl_verify_code"))
    })
    private EmailVerify emailVerify;

}
