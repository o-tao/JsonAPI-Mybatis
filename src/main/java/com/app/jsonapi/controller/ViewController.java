package com.app.jsonapi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@PreAuthorize("hasRole('DEV')") // 클래스 적용 (지정한 권한 접근 허용)
@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    // ADMIN권한의 사용자만 접속 허용
//    @PreAuthorize("hasRole('ADMIN')") // 메소드 적용 (지정한 권한 접근 허용)
    @ResponseBody
    @GetMapping("/admin")
    public String admin(Authentication authentication) {
        return authentication.getName();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
