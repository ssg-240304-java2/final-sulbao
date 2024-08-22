package com.finalproject.sulbao.login.model.service;

import com.finalproject.sulbao.common.file.FileDto;
import com.finalproject.sulbao.common.file.FileService;
import com.finalproject.sulbao.login.model.dto.MemberProfileDto;
import com.finalproject.sulbao.login.model.dto.SignupMemberDto;
import com.finalproject.sulbao.login.model.dto.SignupSellerDto;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.MemberInfo;
import com.finalproject.sulbao.login.model.entity.RoleType;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.repository.MemberInfoRepository;
import com.finalproject.sulbao.login.model.vo.MemberImage;
import com.finalproject.sulbao.login.model.vo.SellerInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class LoginService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final LoginRepository loginRepository;
    private final MemberInfoRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileService fileService;


    // 회원가입
    @Transactional
    public void registNewMember(SignupMemberDto member){

        Login login = Login.builder()
                .userId(member.getUserId())
                .userPw(passwordEncoder.encode(member.getUserPw()))
                .gender(member.getGender())
                .userRole(RoleType.MEMBER)
                .build();
        loginRepository.save(login);

        MemberInfo memberInfo = MemberInfo.builder()
                .profileName(member.getUserId())
                .user(login)
                .build();

        memberRepository.save(memberInfo);
    }

    // 회원가입 - 중복 체크
    public boolean isUserIdExists(String tmpId) {
        return loginRepository.existsByUserId(tmpId);
    }

    // 입점신청
    @Transactional
    public void registNewSeller(SignupSellerDto seller) {

        SellerInfo sellerInfo = new SellerInfo(seller.getBusinessNumber(), seller.getBusinessName(), "WAIT");

        Login login = Login.builder()
                .userId(seller.getBusinessId())
                .userPw(passwordEncoder.encode(seller.getBusinessPw()))
                .userRole(RoleType.SELLER)
                .phone(seller.getBusinessPhone())
                .email(seller.getBusinessEmail())
                .sellerInfo(sellerInfo)
                .build();
        loginRepository.save(login);

    }

    // 입점신청 - 중복체크 사업자번호
    public SellerInfo findSellerByNum(String num) {

        SellerInfo findSeller = loginRepository.findSellerByNum(num);
        System.out.println("ffffffffffinde sellerInfo = " + findSeller);

        return findSeller;
    }

    // 입점신청 - 중복체크 사업자이름
    public boolean isbusinessNameExists(String name) {
        SellerInfo findSeller =  loginRepository.findByBusinessName(name);
        if(findSeller != null) {
            return true;
        }
        return false;
    }

    // 프로필 - 조회
    public MemberProfileDto findMemberByUserId(String userId) {

        Login login = loginRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 입니다."));
        MemberInfo memberInfo = memberRepository.findById(login.getUserNo())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 멤버 입니다."));
        MemberImage profileImag = memberInfo.getMemberImage();

        if(profileImag == null) {
            profileImag = new MemberImage();
            profileImag.setSaveName("default-profile.png");
        }

        String profileName = memberInfo.getProfileName();
        String profileText = memberInfo.getProfileText();
        String email = login.getEmail();
        String birth = login.getBirth();
        String phone = login.getPhone();
        String gender = login.getGender();
        String businessNumber = memberInfo.getProMemberInfo().getBusinessNumber();
        String businessLink = memberInfo.getProMemberInfo().getBusinessLink();
        String date = String.valueOf(memberInfo.getProMemberInfo().getUpdatedAt());

        MemberProfileDto member = new MemberProfileDto(profileImag, profileName, profileText, email, birth, phone, gender, businessNumber, businessLink, date);
        return member;
    }

    // 프로필 - 이름 실시간 중복 체크
    public int isProfileNameDuplicate(String profileName, String userId) {

        Long userNo = loginRepository.findByUserId(userId).orElseThrow().getUserNo();
        System.out.println("userNo = " + userNo);

        return memberRepository.existsByProfileNameAndUserNoNot(profileName, userNo);
    }

    // 프로필 - 업데이트
    @Transactional
    public void updateMemberInfo(MemberProfileDto memberProfile, String userId) {

        Login login = loginRepository.findByUserId(userId).orElseThrow();
        MemberInfo memberInfo = memberRepository.findByUserId(userId);

        // 개인정보 업데이트
        login.setBirth(memberProfile.getBirth());
        login.setEmail(memberProfile.getEmail());
        login.setPhone(memberProfile.getPhone());
        login.setGender(memberProfile.getGender());

        // 프로필 사진 업데이트
        if(!memberProfile.getImage().getOriginalFilename().isEmpty()){
            FileDto fileDto = fileService.uploadFile(memberProfile.getImage(),uploadDir,"sulbao-file/profile");
            MemberImage memberImage = new MemberImage();
            memberImage.setFileName(fileDto.getOriginalFileName());
            memberImage.setSaveName(fileDto.getUploadFileName());
            memberImage.setSaveImgUrl(fileDto.getUploadFileUrl());
            memberInfo.setMemberImage(memberImage);
        }

        // 프로필 이름 업데이트
        if(!memberProfile.getProfileName().isEmpty()){
            memberInfo.setProfileName(memberProfile.getProfileName());
        }
        // 프로필 소개 업데이트
        if(!memberProfile.getProfileText().isEmpty()){
            memberInfo.setProfileText(memberProfile.getProfileText());
        }

    }

    public String findProStatusByUserId(String userId) {
        return memberRepository.findProStatusByUserId(userId);
    }
}
