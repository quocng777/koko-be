package com.quocnguyen.koko.security;

import com.quocnguyen.koko.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author Quoc Nguyen on {8/1/2024}
 */

@Configuration
@RequiredArgsConstructor
public class ComponentSecurityConfig {
    private final UserService userService;


    @Bean
    public UserDetailsService userDetailsService() {
        return (userService::loadByUsername);
    }

    @Bean
    public AppAccessDeniedHandler accessDeniedHandler() {
        return new AppAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AppAuthenticationEntryPoint authenticationEntryPoint() {
        return new AppAuthenticationEntryPoint();
    }
}
