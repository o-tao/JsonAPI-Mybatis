package com.app.jsonapi.controller;

import com.app.jsonapi.dto.UserDto;
import com.app.jsonapi.mapper.AuthMapper;
import com.app.jsonapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@PreAuthorize("hasRole('DEV')") // 클래스 적용 (지정한 권한 접근 허용)
@Slf4j
@Controller
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class ViewController {

    @Autowired
    private AuthMapper authMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;

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
    public String sign(@RequestParam Map<String, String> paramMap, HttpServletRequest request, HttpServletResponse response) {
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

            // 사용자 테이블에 데이터 저장
            int state = authMapper.signup(userDto);

            if (state == 1) {
                log.info("User : {}", paramMap);

                // 사용자 권한 등록
                state = authMapper.roleup(userDto);
                if (state == 1) {
//                    return "redirect:/login"; // 자동 로그인기능 사용 시 주석처리

//                    // 자동 로그인 기능 추가
//                    if (userDto.getUserNm() != null) {
//
//                        // 사용자 정보 데이터 가져오기
//                        UserDetails userDetails = userService.loadUserByUsername(userDto.getUserNm());
//
//                        // 사용자 권한 목록 가져오기
//                        List<RoleDto> roles = authMapper.roles(userDto);
//
//                        // 사용자 전체 정보 생성하기
//                        MyUserDto myUserDto = new MyUserDto(userDto, roles);
//
//                        if (userDetails != null) {
//                            log.info("UserDetails : {}", userDetails);
//
//                            // 회원가입으로 생성한 계정의 토큰 생성
//                            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                                    userDetails.getUsername(),
//                                    userPwd,
//                                    userDetails.getAuthorities());
//
//                            // Security 최상위 객체에 token 전달
//                            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                            log.info("token : {}", token);
//                            log.info("UserDetails : {}", userDetails);
//                            log.info("UserPassword : {}", userPwd);
//                            log.info("Encoded Password from DB : {}", userDetails.getPassword());
//
//                            // 실제 인증 수행
//                            try {
//                                Authentication authResult = authenticationManager.authenticate(token);
//                                SecurityContextHolder.getContext().setAuthentication(authResult);
//                                log.info("Authenticated Token : {}", authResult);
//                                return "redirect:/";
//                            } catch (Exception e) {
//                                log.error("Authentication failed : {}", e.getMessage());
//                                return "redirect:/sign";
//                            }
//                        }
//                    }
                }
            }
        }
        return "redirect:/login";
    }
}
