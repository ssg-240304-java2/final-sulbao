package com.finalproject.sulbao.login;

import com.finalproject.sulbao.login.model.dto.NewMemberDTO;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.MemberInfo;
import com.finalproject.sulbao.login.model.entity.RoleType;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.repository.MemberInfoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RegistTests {


//    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private MemberInfoRepository memberRepository;

    @Test
    @DisplayName("regist-member")
    void registMember() {

        // given
        NewMemberDTO member = new NewMemberDTO("honggd11","1234","M");
        Login login = Login.builder()
                .userId(member.getUserId())
                .userPw(member.getUserPw())
                .gender(member.getGender())
                .userRole(RoleType.MEMBER)
                .build();

        MemberInfo memberInfo = MemberInfo.builder()
                .profileName(member.getUserId())
                .user(login)
                .build();

        // when
        loginRepository.save(login);
        memberRepository.save(memberInfo);

        // then
    }
}
