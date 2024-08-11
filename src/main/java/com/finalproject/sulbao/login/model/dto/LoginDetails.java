package com.finalproject.sulbao.login.model.dto;


import com.finalproject.sulbao.login.model.entity.Login;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class LoginDetails implements UserDetails {

    private final Login login;

    public LoginDetails(Login login) {
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(()->{return "ROLE_"+login.getUserRole();});

        return collectors;
    }

    @Override
    public String getPassword() {
        return login.getUserPw();
    }

    @Override
    public String getUsername() {
        return login.getUserId();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
