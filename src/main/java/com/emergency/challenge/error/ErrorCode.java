package com.emergency.challenge.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {



    //==========================sign up========================================

    SIGNUP_WRONG_MEMBERID(400, "멤버 아이디가 존재하지 않습니다"),
    SIGNUP_WRONG_LOGINID(400, "아이디 형식을 맞춰주세요"),
    SIGNUP_WRONG_NICKNAME(400, "닉네임 형식을 맞춰주세요"),
    SIGNUP_WRONG_PASSWORD(400, "비밀번호 형식을 맞춰주세요"),
    SIGNUP_WRONG_PHONENUMBER(400, "전화번호 형식을 맞춰주세요"),
    ALREADY_SAVED_ID(409, "중복된 아이디입니다."),
    ALERADY_SAVED_NICKNAME(409,"중복된 닉네임입니다."),
    PASSWORDS_NOT_MATCHED(400,"비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    //=============================login=======================================
    MEMBER_NOT_FOUND(404,"사용자를 찾을 수 없습니다."),
    INVALID_MEMBER(404,"사용자를 찾을 수 없습니다."),
    LOGINID_EMPTY(400,"아이디를 입력해주세요"),
    PASSWORD_EMPTY(400,"비밀번호를 입력해주세요"),
    LOGINID_MISMATCH(404,"아이디가 일치하지 않습니다"),
    PASSWORD_MISMATCH(404,"비밀번호가 일치하지 않습니다"),

    //================================token========================================
    INVALID_TOKEN(404,"Token이 유효하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(401, "토근이 만료되었습니다. 다시 로그인 하세요."),

    //================================chat Room=========================================

    NOT_EXIST_ROOM(404,"채팅방이 존재하지 않습니다."),
    ALREADY_EXISTS_CHAT_ROOM(400,"채팅방이 이미 존재합니다."),
    COMMENT_UPDATE_WRONG_ACCESS(400, "본인의 글만 수정할 수 있습니다"),
    COMMENT_DELETE_WRONG_ACCESS(400, "본인의 글만 삭제할 수 있습니다"),

    //==============================500 INTERNAL SERVER ERROR========================

    INTERNAL_SERVER_ERROR(500, "서버 에러입니다. 고객센터에 문의해주세요"),
    BIND_Fails(500,"서버 에러입니다. 고객센터에 문의해주세요");

    private final int status;
    private final String message;
}