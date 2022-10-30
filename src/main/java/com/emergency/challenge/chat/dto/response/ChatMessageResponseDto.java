package com.emergency.challenge.chat.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageResponseDto {

    private Long roomId;
    private Long memberId;

    public ChatMessageResponseDto(Long roomId, Long memberId){
        this.roomId = roomId;
        this.memberId = memberId;
    }
}
