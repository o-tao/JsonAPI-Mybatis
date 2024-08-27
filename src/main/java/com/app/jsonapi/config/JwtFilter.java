package com.app.jsonapi.config;

import com.app.jsonapi.service.UserService;
import com.app.jsonapi.util.Token;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private Token token;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰 검증
        logger.info("JwtFilter Start!");

        String authorization = request.getHeader("Authorization");

        if (authorization != null || !"".equals("Authorization")) {
            log.info("Authorization: {}", authorization);

            if (token.isValidToken(authorization)) {
                log.info("유효한 토큰 입니다.");

                Claims claims = token.getToken(authorization);
                log.info("Claims : {}", claims);

                LinkedHashMap<String, String> user = (LinkedHashMap<String, String>) claims.get("audience");
                log.info("UserDto : {}", user);
                
                UserDetails userDetails = userService.loadUserByUsername(user.get("userNm"));
                UsernamePasswordAuthenticationToken upatoken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(upatoken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
