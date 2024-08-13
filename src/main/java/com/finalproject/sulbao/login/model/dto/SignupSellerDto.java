package com.finalproject.sulbao.login.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupSellerDto {

    @NotBlank(message = "상호명은 한글로 입력가능합니다.")
    @Pattern(regexp = "^[가-힣]+$", message = "상호명은 한글로 입력가능합니다.")
    private String businessName;

    @NotBlank
    private String businessNumber;

    @Pattern(regexp = "^01[0-9]{8,9}$", message = "유효한 휴대폰 번호를 입력해주세요.")
    private String businessPhone;

    private String businessEmail;
    private String confirmEmail;

    @Pattern(regexp = "^[a-z][a-z0-9]{3,15}$", message = "영어 소문자 및 숫자로만, 4~16자리로 가능합니다.")
    private String businessId;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 영어, 숫자, 특수문자 조합 8~16자리로 가능합니다.")
    private String businessPw;

    private String confirmPw;

}
