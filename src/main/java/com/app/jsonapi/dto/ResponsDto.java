package com.app.jsonapi.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponsDto {

    private boolean status;
    private UserDto user;
    private List<RoleDto> roles;
}
