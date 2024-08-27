package com.finalproject.sulbao.login.model.dto;


import com.finalproject.sulbao.login.model.entity.Login;
import com.finalproject.sulbao.login.model.entity.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class LoginDetails implements UserDetails {

    private final Login login;

    public LoginDetails(Login login) {
        this.login = login;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> {
            return "ROLE_" + login.getUserRole();
        });

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

    public Long getUserNo() {
        return login.getUserNo();
    }

    @Override
    public boolean isEnabled() {
        return login.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {

        if(login.getUserRole() == RoleType.SELLER
            && login.getSellerInfo().getSellerStatus().equals("WAIT")){

            return false;
        }
        return UserDetails.super.isAccountNonLocked();
    }


}
