package com.app.jsonapi.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    private String userNm;
    private String userPwd;
}
