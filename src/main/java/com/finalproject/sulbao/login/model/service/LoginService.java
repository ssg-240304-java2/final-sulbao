package com.finalproject.sulbao.login.model.service;

import com.finalproject.sulbao.login.model.dto.MemberProfileDto;
import com.finalproject.sulbao.login.model.dto.SignupMemberDto;
import com.finalproject.sulbao.login.model.dto.SignupSellerDto;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.MemberInfo;
import com.finalproject.sulbao.login.model.entity.RoleType;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.repository.MemberInfoRepository;
import com.finalproject.sulbao.login.model.vo.SellerInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final LoginRepository loginRepository;
    private final MemberInfoRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


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

    public boolean isUserIdExists(String tmpId) {
        return loginRepository.existsByUserId(tmpId);
    }

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

    public SellerInfo findSellerByNum(String num) {

        SellerInfo findSeller = loginRepository.findSellerByNum(num);
        System.out.println("ffffffffffinde sellerInfo = " + findSeller);

        return findSeller;
    }

    public boolean isbusinessNameExists(String name) {
        SellerInfo findSeller =  loginRepository.findByBusinessName(name);
        if(findSeller != null) {
            return true;
        }
        return false;
    }

    public MemberProfileDto findMemberByUserId(String userId) {

        Login login = loginRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 입니다."));
        MemberInfo memberInfo = memberRepository.findById(login.getUserNo())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 멤버 입니다."));

        String email = login.getEmail();
        String phone = login.getPhone();
        String profileImag = memberInfo.getProfileImg();
        String profileName = memberInfo.getProfileName();
        String profileText = memberInfo.getProfileText();

        MemberProfileDto member = new MemberProfileDto(email,phone,profileImag, profileName, profileText);
        return member;
    }
}
