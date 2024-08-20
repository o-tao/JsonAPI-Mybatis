package com.app.jsonapi.controller;

import com.app.jsonapi.util.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonController {

    @Autowired
    private Token token;

    @GetMapping("/token")
    public String token() {
        return token.setToken();
    }
}
