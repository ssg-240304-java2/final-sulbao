package com.finalproject.sulbao.login.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        // 실패 메시지 설정
        String errorMessage = "로그인에 실패했습니다. 다시 시도해주세요.";

        if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "존재하지 않는 아이디입니다.";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "비밀번호가 일치하지 않습니다.";
        }

        // URL 파라미터를 통해 오류 메시지 전달
        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.name());
        response.sendRedirect("/login?error=true&loginError=" + encodedErrorMessage);
    }
}
