package com.finalproject.sulbao.login.model.service;

import com.finalproject.sulbao.login.model.dto.LoginDetails;
import com.finalproject.sulbao.login.model.dto.LoginDto;
import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.repository.LoginRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class LoginDetailsService implements UserDetailsService {

    private final LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        Login login = loginRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 입니다."));
        System.out.println("권한========================" + login.getUserId());
        System.out.println("권한========================" + login.getUserRole());
        return new LoginDetails(login);
//        return new LoginDetails(loginRepository.findByUserId(userId)
//                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자 입니다.")));
    }
}
