package com.emergency.challenge.chat.dto.request;

import com.emergency.challenge.chat.model.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequestDto {

    private ChatMessage.MessageType type; // 메시지 타입

    private Long memberId;  //sender Id

    private String roomId; //ChatRoom Id

    private String name; //ChatRoomName

    private String message; //message

    private LocalDateTime createAt=LocalDateTime.now();

    private String sender; //message 발신자

}
