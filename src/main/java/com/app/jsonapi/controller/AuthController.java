package com.app.jsonapi.controller;

import com.app.jsonapi.dto.ResponsDto;
import com.app.jsonapi.dto.RoleDto;
import com.app.jsonapi.dto.UserDto;
import com.app.jsonapi.mapper.AuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
public class AuthController {

    @Autowired
    private AuthMapper authMapper;

    @PostMapping("/login")
    public ResponsDto login(@RequestParam Map<String, String> loginDto) {
        ResponsDto responsDto = new ResponsDto();
        responsDto.setStatus(false);

        UserDto user = authMapper.login(loginDto);

        // 비정상 사용자 일경우 오류 발생
        if (user == null) {
            return responsDto;
        }

        // 정상 사용자 일경우 권한과 함께 리턴
        List<RoleDto> roles = authMapper.roles(user);
        responsDto.setStatus(true);
        responsDto.setUser(user);
        responsDto.setRoles(roles);
        return responsDto;
    }
}
