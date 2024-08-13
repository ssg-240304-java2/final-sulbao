package com.finalproject.sulbao.login.model.vo;

import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.StatusType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SellerInfo {


    private String businessNumber;

    private String businessName;

    @Enumerated(EnumType.STRING)
    private StatusType sellerStatus;
}
