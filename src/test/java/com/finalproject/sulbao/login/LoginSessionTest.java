package com.finalproject.sulbao.login;

import com.finalproject.sulbao.login.model.vo.LoginVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class LoginSessionTest {

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

}
