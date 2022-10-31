package com.emergency.challenge.chat.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class ChatMessageResponseDto {

    private String roomId;
    private Long memberId;
    private  String message;

    private String name;

    public ChatMessageResponseDto(String roomId, Long memberId,String message,String name){
        this.roomId = roomId;
        this.memberId = memberId;

        this.message=message;
        this.name=name;
    }
}
