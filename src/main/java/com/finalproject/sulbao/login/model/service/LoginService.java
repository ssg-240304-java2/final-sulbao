package com.finalproject.sulbao.login.model.service;

import com.finalproject.sulbao.common.file.FileDto;
import com.finalproject.sulbao.common.file.FileService;
import com.finalproject.sulbao.login.model.dto.*;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.MemberInfo;
import com.finalproject.sulbao.login.model.entity.RoleType;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.repository.MemberInfoRepository;
import com.finalproject.sulbao.login.model.vo.MemberImage;
import com.finalproject.sulbao.login.model.vo.ProMemberInfo;
import com.finalproject.sulbao.login.model.vo.SellerInfo;
import com.finalproject.sulbao.member.dto.MemberDto;
import com.finalproject.sulbao.product.model.entity.Product;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        MemberInfo memberInfo = memberRepository.findByUserId(userId);
//        log.info("sevice member ============================> {}", memberInfo);
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
    public void updateMemberInfo(MemberProfileDto memberProfile, String userId, HttpSession session) {

        Login login = loginRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 입니다."));
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
            session.setAttribute("profileUrl", fileDto.getUploadFileUrl());

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

    // 전문가 신청 유저 정보 조회
    public String findProStatusByUserId(String userId) {
        return memberRepository.findProStatusByUserId(userId);
    }


    // 전문가 신청 저장
    @Transactional
    public void saveProForm(ProFormDto form, String userId) {

        Login login = loginRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 입니다."));
        MemberInfo memberInfo = memberRepository.findByUserId(userId);

//        login.setUserRole(RoleType.PRO_MEMBER);
        login.setEmail(form.getBusinessEmail());

        Long memberNo = memberInfo.getMemberNo();
        String num = form.getBusinessNumber();
        String link = form.getBusinessLink();
        String status = "WAIT";
        ProInfoDto proInfo = new ProInfoDto(memberNo,num,link,status);
        memberRepository.saveProMember(proInfo);
    }

    @Transactional
    public void deleteProForm(String userId) {
        Long memberNo = memberRepository.findByUserId(userId).getMemberNo();
        memberRepository.deleteProForm(memberNo);
    }


    public List<MemberDto> findMemberList() {

        List<Login> members = loginRepository.findAll();
        List<MemberDto> memberList = new ArrayList<>();

        for(Login login : members) {
            MemberDto member = MemberDto.builder()
                    .userNo(login.getUserNo())
                    .userId(login.getUserId())
                    .gender(login.getGender())
                    .email(login.getEmail())
                    .phone(login.getPhone())
                    .role(strRole(login.getUserRole().toString()))
                    .registDate(login.getCreatedAt().toString().substring(0,10))
                    .isAblable(login.isEnabled())
                    .build();
            memberList.add(member);
        }

        return memberList;
    }

    private String strRole(String role) {

        String result ="";
        if (role.equals("MEMBER")){
            result = "일반회원";
        } else if(role.equals("PRO_MEMBER")){
            result = "전문가";
        } else if(role.equals("SELLER")){
            result = "입점사";
        } else {
            result = "관리자";
        }
        return result;
    }

    public List<MemberDto> findProMemberList() {

        List<Login> members = loginRepository.findProMembers();
        List<MemberDto> memberList = new ArrayList<>();

        for(Login login : members) {
            MemberDto member = MemberDto.builder()
                    .userNo(login.getUserNo())
                    .registDate(login.getMemberInfo().getProMemberInfo().getUpdatedAt().toString().substring(0,10))
                    .userId(login.getUserId())
                    .name(login.getMemberInfo().getProfileName())
                    .role(login.getUserRole().toString())
                    .email(login.getEmail())
                    .businessNum(login.getMemberInfo().getProMemberInfo().getBusinessNumber())
                    .businessLink(login.getMemberInfo().getProMemberInfo().getBusinessLink())
                    .status(login.getMemberInfo().getProMemberInfo().getProStatus())
                    .build();
            memberList.add(member);
        }
        return memberList;
    }

    public List<MemberDto> findSellerList() {

        List<Login> sellers = loginRepository.findSellerList();
        List<MemberDto> sellerList = new ArrayList<>();

        for(Login login : sellers) {
            MemberDto seller = MemberDto.builder()
                    .userNo(login.getUserNo())
                    .registDate(login.getCreatedAt().toString().substring(0,10))
                    .userId(login.getUserId())
                    .email(login.getEmail())
                    .phone(login.getPhone())
                    .businessNum(login.getSellerInfo().getBusinessNumber())
                    .businessName(login.getSellerInfo().getBusinessName())
                    .status(login.getSellerInfo().getSellerStatus())
                    .isAblable(login.isEnabled())
                    .build();
            sellerList.add(seller);
        }
        return sellerList;
    }

    // 회원 비활성화
    @Transactional
    public void updateEnable(String memberList, String type, String available) {

        String[] memberNoArray = memberList.split(",");

        for(String userNo : memberNoArray) {
            Optional<Login> login = loginRepository.findByUserNo(userNo);

            if(login.isPresent()) {
                if(available.equals("true")){
                    login.get().setEnabled(true);
                } else {
                    login.get().setEnabled(false);
                }
            }
        }
    }

    // 전문가 승인
    @Transactional
    public void updateProStatus(String memberList, String type, String proStatus) {

        String[] memberNoArray = memberList.split(",");

        for(String userNo : memberNoArray) {
            Optional<Login> login = loginRepository.findByUserNo(userNo);

            if(login.isPresent()) {

                ProMemberInfo proMember = login.get().getMemberInfo().getProMemberInfo();
                if(proStatus.equals("approve")){
                    proMember.setStatusApprove();
                    login.get().setApprovePro();
                }
            }
        }
    }

    // 입점 승인
    @Transactional
    public void updateSellStatus(String sellerList, String type, String sellStatus) {
        String[] memberNoArray = sellerList.split(",");

        for(String userNo : memberNoArray) {
            Optional<Login> login = loginRepository.findByUserNo(userNo);

            if(login.isPresent()) {

                SellerInfo seller = login.get().getSellerInfo();
                if(sellStatus.equals("approve")){
                    seller.setStatusApprove();
                }
            }
        }
    }

    public List<MemberDto> findMemberBySearch(String searchType, String searchInput) {

        List<MemberDto> memberDtoList = new ArrayList<>();
        List<Login> loginlist = new ArrayList<>();


        if(searchType.equals("userId")){
            loginlist =  loginRepository.findMemberBySearchUserId(searchInput);
        } else if(searchType.equals("name")){
            loginlist =  loginRepository.findMemberBySearchName(searchInput);
        } else if(searchType.equals("email")){
            loginlist =  loginRepository.findMemberBySearchEmail(searchInput);
        }

        for(Login login : loginlist) {
            MemberDto member = MemberDto.builder()
                    .userNo(login.getUserNo())
                    .userId(login.getUserId())
                    .gender(login.getGender())
                    .email(login.getEmail())
                    .phone(login.getPhone())
                    .role(strRole(login.getUserRole().toString()))
                    .registDate(login.getCreatedAt().toString().substring(0,10))
                    .isAblable(login.isEnabled())
                    .build();
            memberDtoList.add(member);
        }


        return memberDtoList;
    }

    public List<MemberDto> findMemberByRole(String roleCategory) {
        RoleType role = RoleType.MEMBER;

        switch (roleCategory){
            case "일반" : role = RoleType.MEMBER; break;
            case "전문가" : role = RoleType.PRO_MEMBER; break;
            case "판매자" : role = RoleType.SELLER; break;
            case "관리자" : role = RoleType.ADMIN; break;
        }


        List<MemberDto> memberDtoList = new ArrayList<>();
        List<Login> loginlist = loginRepository.findMemberByRole(role);

        for(Login login : loginlist) {
            MemberDto member = MemberDto.builder()
                    .userNo(login.getUserNo())
                    .userId(login.getUserId())
                    .gender(login.getGender())
                    .email(login.getEmail())
                    .phone(login.getPhone())
                    .role(strRole(login.getUserRole().toString()))
                    .registDate(login.getCreatedAt().toString().substring(0,10))
                    .isAblable(login.isEnabled())
                    .build();
            memberDtoList.add(member);
        }

        return memberDtoList;
    }

    public List<MemberDto> findProMemberBySearch(String searchType, String searchInput) {

        List<MemberDto> memberDtoList = new ArrayList<>();
        List<Login> loginlist = new ArrayList<>();


        if(searchType.equals("userId")){
            loginlist =  loginRepository.findProMemberBySearchUserId(searchInput);
        } else if(searchType.equals("name")){
            loginlist =  loginRepository.findProMemberBySearchName(searchInput);
        } else if(searchType.equals("email")){
            loginlist =  loginRepository.findProMemberBySearchEmail(searchInput);
        } else if(searchType.equals("number")){
            loginlist =  loginRepository.findProMemberBySearchBusinessNum(searchInput);
        }

        for(Login login : loginlist) {
            MemberDto member = MemberDto.builder()
                    .userNo(login.getUserNo())
                    .userId(login.getUserId())
                    .gender(login.getGender())
                    .email(login.getEmail())
                    .phone(login.getPhone())
                    .role(strRole(login.getUserRole().toString()))
                    .registDate(login.getCreatedAt().toString().substring(0,10))
                    .isAblable(login.isEnabled())
                    .build();
            memberDtoList.add(member);
        }

        return memberDtoList;
    }

    public List<MemberDto> findProMemberByStatus(String statusAll) {

        List<MemberDto> memberDtoList = new ArrayList<>();
        List<Login> loginlist = loginRepository.findProMemberByStatus(statusAll);

        for(Login login : loginlist) {
            MemberDto member = MemberDto.builder()
                    .userNo(login.getUserNo())
                    .userId(login.getUserId())
                    .gender(login.getGender())
                    .email(login.getEmail())
                    .phone(login.getPhone())
                    .role(strRole(login.getUserRole().toString()))
                    .registDate(login.getCreatedAt().toString().substring(0,10))
                    .isAblable(login.isEnabled())
                    .build();
            memberDtoList.add(member);
        }

        return memberDtoList;
    }

    public List<MemberDto> findSellerBySearch(String searchType, String searchInput) {

        List<MemberDto> memberDtoList = new ArrayList<>();
        List<Login> loginlist = new ArrayList<>();

        if(searchType.equals("userId")){
            loginlist =  loginRepository.findSellerBySearchUserId(searchInput);
        } else if(searchType.equals("name")){
            loginlist =  loginRepository.findSellerBySearchName(searchInput);
        } else if(searchType.equals("email")){
            loginlist =  loginRepository.findSellerBySearchEmail(searchInput);
        } else if(searchType.equals("number")){
            loginlist =  loginRepository.findSellerBySearchBusinessNum(searchInput);
        }

        for(Login login : loginlist) {
            MemberDto seller = MemberDto.builder()
                    .userNo(login.getUserNo())
                    .registDate(login.getCreatedAt().toString().substring(0,10))
                    .userId(login.getUserId())
                    .email(login.getEmail())
                    .phone(login.getPhone())
                    .businessNum(login.getSellerInfo().getBusinessNumber())
                    .businessName(login.getSellerInfo().getBusinessName())
                    .status(login.getSellerInfo().getSellerStatus())
                    .isAblable(login.isEnabled())
                    .build();
            memberDtoList.add(seller);
        }

        return memberDtoList;
    }

    public List<MemberDto> findSellerByStatus(String statusAll) {

        List<MemberDto> memberDtoList = new ArrayList<>();
        List<Login> loginlist = loginRepository.findSellerByStatus(statusAll);

        for(Login login : loginlist) {
            MemberDto seller = MemberDto.builder()
                    .userNo(login.getUserNo())
                    .registDate(login.getCreatedAt().toString().substring(0,10))
                    .userId(login.getUserId())
                    .email(login.getEmail())
                    .phone(login.getPhone())
                    .businessNum(login.getSellerInfo().getBusinessNumber())
                    .businessName(login.getSellerInfo().getBusinessName())
                    .status(login.getSellerInfo().getSellerStatus())
                    .isAblable(login.isEnabled())
                    .build();
            memberDtoList.add(seller);
        }

        return memberDtoList;

    }

    public List<MemberDto> findMemberByAvailable(String availableYn) {

        List<MemberDto> memberDtoList = new ArrayList<>();
        List<Login> loginlist = loginRepository.findMemberByAvailable(availableYn);

        for(Login login : loginlist) {
            MemberDto member = MemberDto.builder()
                    .userNo(login.getUserNo())
                    .userId(login.getUserId())
                    .gender(login.getGender())
                    .email(login.getEmail())
                    .phone(login.getPhone())
                    .role(strRole(login.getUserRole().toString()))
                    .registDate(login.getCreatedAt().toString().substring(0,10))
                    .isAblable(login.isEnabled())
                    .build();
            memberDtoList.add(member);
        }

        return memberDtoList;
    }
}
