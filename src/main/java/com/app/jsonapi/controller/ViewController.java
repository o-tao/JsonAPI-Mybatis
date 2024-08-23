package com.app.jsonapi.controller;

import com.app.jsonapi.dto.UserDto;
import com.app.jsonapi.mapper.AuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

//@PreAuthorize("hasRole('DEV')") // 클래스 적용 (지정한 권한 접근 허용)
@Slf4j
@Controller
public class ViewController {

    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    // ADMIN권한의 사용자만 접속 허용
//    @PreAuthorize("hasRole('ADMIN')") // 메소드 적용 (지정한 권한 접근 허용)
    @ResponseBody
    @GetMapping("/admin")
    public String admin(Authentication auth) {
        log.info("auth : {}", auth);
        return auth.getName();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/sign")
    public String sign() {
        return "sign";
    }

    @PostMapping("/sign")
    public String sign(@RequestParam Map<String, String> paramMap) {
        log.info("New User : {}", paramMap);

        if (paramMap != null) {
            String userPwd = paramMap.get("userPwd");
            String userNm = paramMap.get("userNm");

            // 사용자 비밀번호 암호화 처리
            if (userPwd != null) {
                userPwd = passwordEncoder.encode(userPwd);
                paramMap.put("userPwd", userPwd);
            }
            // 사용자 정보 DTO로 변경
            UserDto userDto = new UserDto();
            userDto.setUserNm(userNm);
            userDto.setUserPwd(userPwd);
            int state = authMapper.signup(userDto);

            if (state == 1) {
                log.info("User : {}", paramMap);
                return "redirect:/login";
            }
        }
        return "redirect:/sign";
    }
}
