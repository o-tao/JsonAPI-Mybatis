package com.app.jsonapi.controller;

import com.app.jsonapi.util.Token;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class JsonController {

    @Autowired
    private Token token;

    @GetMapping("/test")
    public Object test(Authentication authentication) {
//        return authentication.getName(); // 사용자 이름 출력
        return authentication.getPrincipal(); // 사용자 정보 전체 출력
    }

    @GetMapping("/token")
    public String token(Authentication auth) {
//        log.info("Auth : {}", auth.getPrincipal());
        return token.setToken(auth);
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
