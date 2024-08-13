package com.finalproject.sulbao.login.model.vo;
import com.finalproject.sulbao.login.model.entity.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProMemberInfo {

    private String businessNumber;
    private String businessLink;
//    @Enumerated(EnumType.STRING)
    private StatusType proStatus;
    private LocalDateTime proRegistDate;
}
