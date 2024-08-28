package com.app.jsonapi.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class MyUserDto implements UserDetails {

    UserDto user;
    List<RoleDto> roles;

    public MyUserDto(UserDto user, List<RoleDto> roles) {
        this.user = user;
        this.roles = roles;
    }

    // 권한
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grant = new HashSet<>();
        roles.forEach(role ->
        {
            if (role.getRoleNm().equals("ADMIN")) {
                grant.add(new SimpleGrantedAuthority(user.getUserNm() + "님은 ROLE_" + role.getRoleNm() + "(관리자) 권한을 가지고 있습니다."));
            }
            if (role.getRoleNm().equals("USER")) {
                grant.add(new SimpleGrantedAuthority(user.getUserNm() + "님은 ROLE_" + role.getRoleNm() + "(일반 사용자) 권한을 가지고 있습니다."));
            }
        });

        return grant;
    }

    // password
    @Override
    public String getPassword() {
        return user.getUserPwd();
    }

    // name
    @Override
    public String getUsername() {
        Set<GrantedAuthority> name = new HashSet<>();
        roles.forEach(role -> {
            if (role.getRoleNm().equals("ADMIN")) {
                name.add(new SimpleGrantedAuthority(user.getUserNm() + " 관리자님 안녕하세요."));
            }
            if (role.getRoleNm().equals("USER")) {
                name.add(new SimpleGrantedAuthority(user.getUserNm() + " 님 안녕하세요."));
            }
        });

        return name.toString();
    }
}
