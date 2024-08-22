package com.app.jsonapi.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        roles.forEach(role -> grant.add(new SimpleGrantedAuthority(role.getRoleNm())));
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
        return user.getUserNm();
    }
}
