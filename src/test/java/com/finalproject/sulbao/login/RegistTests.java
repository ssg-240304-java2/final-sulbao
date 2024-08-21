package com.finalproject.sulbao.login;

import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import com.finalproject.sulbao.login.model.repository.MemberInfoRepository;
import org.assertj.core.api.ObjectAssert;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RegistTests {


//    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private MemberInfoRepository memberRepository;

//    @Test
//    @DisplayName("regist-member")
//    void registMember() {
//
//        // given
//        NewMemberDTO member = new NewMemberDTO("honggd11","1234","M");
//        Login login = Login.builder()
//                .userId(member.getUserId())
//                .userPw(member.getUserPw())
//                .gender(member.getGender())
//                .userRole(RoleType.MEMBER)
//                .build();
//
//        MemberInfo memberInfo = MemberInfo.builder()
//                .profileName(member.getUserId())
//                .user(login)
//                .build();
//
//        // when
//        loginRepository.save(login);
//        memberRepository.save(memberInfo);
//
//        // then
//    }

    @Test
    public void testFindByUserIdAndCheckUserNo() {
        // Given
        String userId = "yeon915";
        Long expectedUserNo = 17L;

        // When
        Optional<Login> optionalLogin = loginRepository.findByUserId(userId);

        // Then
        assertTrue(optionalLogin.isPresent(), "User should be found in the database");

        Login login = optionalLogin.get();
        assertEquals(expectedUserNo, login.getUserNo(), "UserNo should match the expected value");
    }
}
