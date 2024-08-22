package com.app.jsonapi.service;

import com.app.jsonapi.dto.MyUserDto;
import com.app.jsonapi.dto.RoleDto;
import com.app.jsonapi.dto.UserDto;
import com.app.jsonapi.mapper.AuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AuthMapper authMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserName {}", username);
        // Optional 처리
        UserDto user = authMapper.user(username).orElseThrow(() -> new UsernameNotFoundException("사용자 정보가 없습니다."));

        // UserDto 예외처리
//        if (user == null) {
//            throw new UsernameNotFoundException("사용자 정보가 없습니다.");
//        }
        log.info("User {}", user);

        // 권한 조회
        List<RoleDto> roles = authMapper.roles(user);
        roles.forEach(System.out::println); // 권한 출력


        return new MyUserDto(user, roles);
    }
}
