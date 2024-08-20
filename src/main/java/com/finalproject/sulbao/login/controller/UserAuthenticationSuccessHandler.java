package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.login.model.dto.LoginDetails;
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
import java.util.stream.Collectors;

@Component
@Getter
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private String userId;
    @Setter
    private String defaultUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginDetails login = (LoginDetails) authentication.getPrincipal();

        String userId = login.getUsername();
        Long userNo = login.getUserNo();
        String role = login.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

//        System.out.println("id =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + userNo);
//        System.out.println("id =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + userId);
//        System.out.println("role =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + role);


        HttpSession session = request.getSession();
        session.setAttribute("userNo", userNo);
        session.setAttribute("userId", userId);
        session.setAttribute("role", role);
        session.setMaxInactiveInterval(3600); // Session이 60분동안 유지


        if(role.equals("ROLE_MEMBER")) {
            response.sendRedirect("/");
        } else if (role.equals("ROLE_SELLER")){

            response.sendRedirect("/auth/testSeller");  // 셀러 로그인 후 이동 페이지
        } else if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/auth/testadmin");   // 어드민 로그인 후 이동 페이지
        }

    }
}
