package com.finalproject.sulbao.board.common;

import com.finalproject.sulbao.board.dto.UserDto;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionHandler {

    private final LoginRepository loginRepository;

    public boolean isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getAttribute("userNo") != null;
    }

    public UserDto getUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object userNo = session.getAttribute("userNo");
        if (userNo == null) {
            return UserDto.getAnonymous();
        }

        Long userId = (Long) userNo;
        Login login = loginRepository.findById(userId).orElseThrow();
        return UserDto.toUserDto(login);
    }

}
