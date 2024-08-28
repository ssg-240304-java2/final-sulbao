package com.finalproject.sulbao.board.common;

import com.finalproject.sulbao.board.dto.UserDto;
import com.finalproject.sulbao.cart.dto.SellerDto;
import com.finalproject.sulbao.login.model.dto.LoginDetails;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionHandler {

    private final LoginRepository loginRepository;

    public boolean isLogin(Authentication authentication) {
        return authentication != null;
    }

    public Long getUserId(Authentication authentication) {
        return ((LoginDetails) authentication.getPrincipal()).getUserNo();
    }

    public UserDto getUser(Authentication authentication) {
        if (isLogin(authentication)) {
            Long userId = ((LoginDetails) authentication.getPrincipal()).getUserNo();
            Login user = loginRepository.findById(userId).orElseThrow();
            return UserDto.toUserDto(user);
        } else {
            return UserDto.getAnonymous();
        }
    }

    public SellerDto getSeller(Authentication authentication) {
        if(isLogin(authentication)) {
            Long userId = ((LoginDetails) authentication.getPrincipal()).getUserNo();
            Login seller = loginRepository.findById(userId).orElseThrow();
            return SellerDto.toUserDto(seller);
        } else {
            return SellerDto.getAnonymous();
        }
    }

}
