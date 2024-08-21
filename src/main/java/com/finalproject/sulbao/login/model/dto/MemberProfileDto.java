package com.finalproject.sulbao.login.model.dto;


import com.finalproject.sulbao.login.model.vo.MemberImage;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
public class MemberProfileDto {
    private MemberImage profileImg;
    private MultipartFile image;
    private String profileName;
    private String profileText;
    private String email;
    private String emailCode;
    private String birth;
    private String phone;
    private String gender;
    private String businessNumber;
    private String businessLink;
    private String date;

    public MemberProfileDto(MemberImage profileImg, String profileName, String profileText, String email, String birth, String phone, String gender, String businessNumber, String businessLink, String date) {
        this.profileImg = profileImg;
        this.profileName = profileName;
        this.profileText = profileText;
        this.email = email;
        this.birth = birth;
        this.phone = phone;
        this.gender = gender;
        this.businessNumber = businessNumber;
        this.businessLink = businessLink;
        this.date = date;
    }
}
