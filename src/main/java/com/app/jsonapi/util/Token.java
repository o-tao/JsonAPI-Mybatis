package com.app.jsonapi.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class Token {

    public String setToken() {

        JwtBuilder token = Jwts.builder()
                .header().add(getHeader()).and()
                .signWith(getSecretKey());

        log.info("Token : {}", token.compact());
        return token.compact();
    }

    // header
    private Map<String, Object> getHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        return header;
    }

    // signWith
    private SecretKey getSecretKey() {
        return Jwts.SIG.HS256.key().build();
    }
}
