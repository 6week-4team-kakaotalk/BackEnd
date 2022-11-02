package com.emergency.challenge.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfo {

    private Long memberId;
    private String loginId;
    private String nickName;
    private String password;
    private String phoneNumber;

    public MemberInfo(Member member){
        this.memberId = member.getMemberId();
        this.loginId = member.getLoginId();
        this.nickName = member.getNickName();
        this.password = member.getPassword();
        this.phoneNumber = member.getPhoneNumber();

    }

}
