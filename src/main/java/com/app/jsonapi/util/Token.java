package com.app.jsonapi.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class Token {

    private final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;

    @Value("${access.auth.jwt}")
    private String jwtSecretKey;

    @Value("${access.auth.interval}")
    private int interval;


    public String setToken() {

        JwtBuilder token = Jwts.builder()
                .header().add(getHeader()).and()
                .claims(setClaims())
                .signWith(getSecretKey(), ALGORITHM);

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
//        return Jwts.SIG.HS256.key().build(); // 토큰 생성
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecretKey)); // 암호화 된 토큰 디코드
//        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes()); // 35자 생성한 토큰
    }

    // claims
    private Map<String, Object> setClaims() {
        Map<String, Object> claims = new HashMap<>();

        Map<String, Object> user = new HashMap<>();
        user.put("name", "사용자"); // DB에서 받아오는 정보

        claims.put("issuer", "JsonAPI"); // 발급자 또는 발급기관
        claims.put("subject", "user"); // 제목 또는 식별자
        claims.put("audience", user); // 대상자 또는 사용자
        claims.put("expiration", getDate()); // 만료 또는 종료시간
        claims.put("issuedAt", Calendar.getInstance().getTime()); // 발급 또는 발행시간

        return claims;
    }

    // ~분동안 유지할 수 있는 토큰
    private Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, interval);
        return calendar.getTime();
    }
}
