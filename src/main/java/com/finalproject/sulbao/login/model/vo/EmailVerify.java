package com.finalproject.sulbao.login.model.vo;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerify {

    private String verifyCode;
    private boolean isVerified;
}
