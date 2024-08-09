package com.finalproject.sulbao.login.model.vo;

import com.amazonaws.services.dynamodbv2.xspec.S;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SellerInfo {


    private String businessNumber;

    private String businessName;

    private String sellerStatus;
}