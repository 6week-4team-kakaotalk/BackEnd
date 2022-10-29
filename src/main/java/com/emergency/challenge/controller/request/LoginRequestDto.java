package com.emergency.challenge.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    // email 아이디로 로그인
    private String loginId;
    private String password;

}
