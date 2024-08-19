package com.finalproject.sulbao.config;

import com.finalproject.sulbao.login.controller.UserAuthenticationFailureHandler;
import com.finalproject.sulbao.login.controller.UserAuthenticationSuccessHandler;
import com.finalproject.sulbao.login.model.service.LoginDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final LoginDetailsService loginService;
    private final UserAuthenticationFailureHandler failureHandler;
    private final UserAuthenticationSuccessHandler successHandler;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/user/css/**","/user/js/**","/user/images/**", "/user/fonts/**"
                                                    , "/admin/css/**","/admin/js/**","/admin/images/**", "/admin/data/**", "/admin/plugins/**");
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
                    .requestMatchers("/payments/**").permitAll()
                    .requestMatchers("/**").permitAll()
//                    .requestMatchers("/testseller").hasRole("SELLER")
//                    .requestMatchers("/testadmin").hasRole("ADMIN");
                    .anyRequest().authenticated();
        }));

        http.formLogin((formLoginConfigurer -> {
            formLoginConfigurer
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .loginProcessingUrl("/auth/login")
                    .failureHandler(failureHandler)
                    .successHandler(successHandler)
                    .usernameParameter("userId")
                    .passwordParameter("userPw")
                    .permitAll();

        }));

        // 로그아웃 설정
        http.logout(logoutConfigurer -> {
            logoutConfigurer
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true);
        });

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, LoginDetailsService loginDetailsService) throws Exception{
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(loginDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

}
