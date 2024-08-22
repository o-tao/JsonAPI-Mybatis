package com.app.jsonapi.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private int userNo;
    private String userNm;
    private String userPwd;
    private boolean userEnable;
}
