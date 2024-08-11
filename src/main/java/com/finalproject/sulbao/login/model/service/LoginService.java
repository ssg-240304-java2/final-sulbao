package com.finalproject.sulbao.login.model.service;

import com.finalproject.sulbao.login.model.dto.SignupMemberDto;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.MemberInfo;
import com.finalproject.sulbao.login.model.entity.RoleType;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.repository.MemberInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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


}
