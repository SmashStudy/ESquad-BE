package com.esquad.esquadbe.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                );

        http
                .formLogin((auth) -> auth
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .defaultSuccessUrl("/")
                        .permitAll()
                );

        http
                // 개발 환경에서 csrf.disable
                 .csrf(AbstractHttpConfigurer::disable);

                /*
                 csrf 허용
                 .csrf((auth) -> auth
                         .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
                */

        http
                .sessionManagement((auth) -> auth
                        // 하나의 아이디에 대한 다중 로그인 허용 개수
                        .maximumSessions(1)
                        // true: 초과시 새로운 로그인 차단
                        // false: 초과시 기존 세션 하나 삭제
                        .maxSessionsPreventsLogin(true));

        http
                .sessionManagement((auth) -> auth.sessionFixation().changeSessionId());

        return http.build();
    }

}