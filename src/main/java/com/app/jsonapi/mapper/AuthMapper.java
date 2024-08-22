package com.app.jsonapi.mapper;

import com.app.jsonapi.dto.RoleDto;
import com.app.jsonapi.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface AuthMapper {

    @Select("select userNo, userNm from company.user where userEnable = 1 and userNm = #{userNm} and userPwd = #{userPwd} ")
    UserDto login(Map<String, String> loginDto);

    @Select("select roleNm from company.user_role as ur inner join role as r where ur.roleNo = r.roleNo and ur.userNo = #{userNo} ")
    List<RoleDto> roles(UserDto userDto);

    @Select("select userNo, userNm, userPwd, userEnable from company.user where userNm = #{userNm} ")
    Optional<UserDto> user(String userNm);
}
