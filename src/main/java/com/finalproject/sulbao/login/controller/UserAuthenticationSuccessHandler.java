package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.LoginDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Getter
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private String userId;
    @Setter
    private String defaultUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        LoginDetails login  = (LoginDetails) authentication.getPrincipal();

        String userId = login.getUsername();
        String role = login.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        System.out.println("id =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + userId);
        System.out.println("role =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + role);

        if(role.equals("ROLE_MEMBER")) {
            response.sendRedirect("/");
        } else if (role.equals("ROLE_SELLER")){
            response.sendRedirect("/auth/testSeller");   // 이부분 수정해줘
        }

    }
}
