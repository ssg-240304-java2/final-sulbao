package com.finalproject.sulbao.login.security.config;

import com.finalproject.sulbao.login.model.service.LoginDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final LoginDetailsService loginService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/user/css/**","/user/js/**","/user/images/**", "/user/fonts/**"
                                                    , "/admin/css/**","/admin/js/**","/admin/images/**", "/admin/data/**", "/admin/plugins/**");
//        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    // 패스워드 인코더
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/", "/index").permitAll()
                    .requestMatchers("/**").permitAll()
                    .anyRequest().authenticated();
        }));

        http.formLogin((formLoginConfigurer -> {
            formLoginConfigurer
                    .loginPage("/login")    // loginform페이지 (GET) (핸들러메소드작업필요)
                    .loginProcessingUrl("/login")    // 로그인처리(POST)
                    .usernameParameter("userid")
                    .passwordParameter("userPw")
                    .permitAll();
        }));

        // 로그아웃 설정
        http.logout(logoutConfigurer -> {
            logoutConfigurer
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/");
        });

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception{
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(loginService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

}
