package com.app.jsonapi.controller;

import com.app.jsonapi.util.Token;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonController {

    @Autowired
    private Token token;

    @GetMapping("/token")
    public String token() {
        return token.setToken();
    }

    @GetMapping("/token/{token}")
    public Claims token(@PathVariable("token") String tokenKey) {
        if (token.isValidToken(tokenKey)) {
            return token.getToken(tokenKey);
        }
        return null;
    }
}
