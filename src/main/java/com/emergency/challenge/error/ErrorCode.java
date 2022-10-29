package com.emergency.challenge.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),
    PASSWORDS_NOT_MATCHED(400,"비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    //404 NOT_FOUND 찾지 못함
    MEMBER_NOT_FOUND(404,"사용자를 찾을 수 없습니다."),
    INVALID_MEMBER(404,"사용자를 찾을 수 없습니다."),
    INVALID_TOKEN(404,"Token이 유효하지 않습니다."),
    //409 CONFLICT 중복된 리소스
    ALREADY_SAVED_ID(409, "중복된 아이디입니다."),
    ALERADY_SAVED_NICKNAME(409,"중복된 닉네임입니다."),

    //500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 고객센터에 문의해주세요"),
    BIND_Fails(500,"서버 에러입니다. 고객센터에 문의해주세요");

    private final int status;
    private final String message;
}