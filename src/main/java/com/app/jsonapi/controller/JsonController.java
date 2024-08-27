package com.app.jsonapi.controller;

import com.app.jsonapi.dto.TokenDto;
import com.app.jsonapi.dto.UserDto;
import com.app.jsonapi.mapper.AuthMapper;
import com.app.jsonapi.util.Token;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class JsonController {

    @Autowired
    private Token token;
    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public Object test(Authentication authentication) {
//        return authentication.getName(); // 사용자 이름 출력
        return authentication.getPrincipal(); // 사용자 정보 전체 출력
    }

    @PostMapping("/jsLogin")
    public TokenDto jsLogin(@RequestParam Map<String, String> params) {
        String userPwd = params.get("userPwd");
        boolean state = false;
        String jwt = null;

        if (userPwd != null) {
            UserDto userDto = authMapper.user(params.get("userNm")).orElseThrow();

            if (passwordEncoder.matches(userPwd, userDto.getUserPwd())) {
                log.info("동일한 값");
                state = true;
                jwt = token.setToken(userDto);
            }
        }
        return TokenDto.builder()
                .state(state)
                .token(jwt)
                .build();
    }

    @GetMapping("/token")
    public String token(Authentication auth) {
//        log.info("Auth : {}", auth.getPrincipal());
        return token.setToken(auth);
    }

    @PostMapping("/getUser")
    public String getUser(HttpServletRequest request) {
        return check(request);
    }

    private String check(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        log.info("Authorization: {}", authorization);

        if (token.isValidToken(authorization)) {
            Claims claims = token.getToken(authorization);
            Map<String, String> user = (Map<String, String>) claims.get("audience");
            log.info("claims: {}", user.get("userNm"));
            return user.get("userNm");
        }
        return null;
    }

    @PostMapping("/token")
    public Claims getToken(@RequestParam("token") String jwt) {
        if (token.isValidToken(jwt)) {
            return token.getToken(jwt);
        }
        return null;
    }

    @GetMapping("/token/{token}")
    public Claims token(@PathVariable("token") String tokenKey) {
        if (token.isValidToken(tokenKey)) {
            return token.getToken(tokenKey);
        }
        return null;
    }
}
