package com.finalproject.sulbao.login.controller;

import com.finalproject.sulbao.cart.service.CartService;
import com.finalproject.sulbao.login.model.dto.LoginDetails;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
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

    private final LoginRepository repository;
    private final CartService cartService;

    public UserAuthenticationSuccessHandler(LoginRepository repository, CartService cartService) {
        this.repository = repository;
        this.cartService = cartService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginDetails login = (LoginDetails) authentication.getPrincipal();

        String userId = login.getUsername();
        Long userNo = login.getUserNo();
        String role = login.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String profileUrl = repository.findProfileUrflByUserNo(userNo);

        HttpSession session = request.getSession();
        session.setAttribute("userNo", userNo);
        session.setAttribute("userId", userId);
        session.setAttribute("role", role);
        session.setAttribute("profileUrl", defaultUrlCheck(profileUrl));
        session.setMaxInactiveInterval(3600); // Session이 60분동안 유지
        int cartList = cartService.findCartCountByUserId(userId);
        session.setAttribute("cartList", cartList);

        if(role.equals("ROLE_MEMBER")) {
            response.sendRedirect("/");
        } else if (role.equals("ROLE_PRO_MEMBER")){
            response.sendRedirect("/");
        } else if (role.equals("ROLE_SELLER")){
            response.sendRedirect("/main");  // 셀러 로그인 후 이동 페이지
        } else if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/main");   // 어드민 로그인 후 이동 페이지
        }
    }

    private String defaultUrlCheck(String profileUrl) {

        if(profileUrl == null || profileUrl.isEmpty()){
            profileUrl = "https://kr.object.ncloudstorage.com/sulbao-file/profile/default-profile.png";
        }
        return profileUrl;
    }
}
