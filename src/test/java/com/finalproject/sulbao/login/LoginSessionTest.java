package com.finalproject.sulbao.login;

import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.service.LoginService;
import com.finalproject.sulbao.login.model.vo.LoginVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
public class LoginSessionTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    LoginService loginService;

    @DisplayName("VO 동등비교를 한다.")
    @Test
    void isSameObjects() {
        LoginVO user1 = LoginVO.builder().userId("test001").userPw("1234").userNo(2L).build();
        LoginVO user2 = LoginVO.builder().userId("test001").userPw("1234").userNo(2L).build();

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).hasSameHashCodeAs(user2);
    }

    @DisplayName("VO 같지 않다. 테스트")
    @Test
    void isNotSameObjects() {
        LoginVO user1 = LoginVO.builder().userId("test00155555").userPw("1234").userNo(2L).build();
        LoginVO user2 = LoginVO.builder().userId("test001").userPw("1234").userNo(2L).build();

        assertThat(user1).isNotSameAs(user2);
    }

    private MockMvc mockMvc;

//    @DisplayName("로그인 성공 테스트")
//    @Test
//    void loginInvalidUser() throws Exception {
//        // given
//        String userId = "yeon915";
//        String userPw = "qwer1234!";
//
//
//        // when
//        mockMvc.perform(formLogin("/login/member"))
//                .user()
//
//        // then
//    }


}
