package com.finalproject.sulbao.login.model.vo;
import com.finalproject.sulbao.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProMemberInfo extends BaseEntity {

    private String businessNumber;
    private String businessLink;
    private String proStatus;
}
