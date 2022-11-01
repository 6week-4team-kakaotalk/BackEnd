package com.emergency.challenge.controller.response;

import com.emergency.challenge.domain.Friend;
import com.emergency.challenge.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    // email 아이디
    private String loginId;
    //추가된 name 속성
    private String nickName;
    private String phoneNumber;
    private List<Friend> friends;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
