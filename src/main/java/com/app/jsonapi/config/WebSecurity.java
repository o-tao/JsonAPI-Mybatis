package com.app.jsonapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(req -> {
            req.requestMatchers("/").permitAll(); // 모든 사용자 접근 가능
            req.requestMatchers("/admin").hasRole("ADMIN"); // ADMIN 사용자 접근 가능
            req.anyRequest().authenticated(); // 정의한 내역 제외 모두 접근 불가
        });

//        http.formLogin(Customizer.withDefaults()); // 로그인화면 접근제어

        // 로그인화면 접근제어 (컬럼명 변경하여 사용)
        http.formLogin(login -> {
            login.loginPage("/login");
            login.defaultSuccessUrl("/admin", false); // 로그인 성공시 redirect -> false: 접속하고자 하는 URL / true: 기본정의한 URL
            login.failureUrl("/"); // 로그인 오류시 redirect
            login.usernameParameter("userNm");
            login.passwordParameter("userPwd");

            // 로그인 성공시 redirect (defaultSuccessUrl 보다 우선순위)
            login.successHandler((request, response, auth) -> {
                log.info("User : {}", auth.getPrincipal());
                response.sendRedirect("/");
            });

            // 로그인 오류시 redirect (failureUrl 보다 우선순위)
            login.failureHandler((request, response, auth) -> {
                response.sendRedirect("/login");
            });

            login.permitAll(); // 모든 사용자 접근 가능
        });

        http.logout(logout -> logout.logoutSuccessUrl("/")); // 로그아웃 성공시 redirect

        return http.build();
    }

//    @Bean
//    public UserDetailsService users() {
//        log.info("PasswordEncode {}", passwordEncoder().encode("1234")); // 암호화처리 패스워드 확인
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("1234"))
//                .roles("DEV")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("1234"))
//                .roles("USER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    // password 인코더
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
