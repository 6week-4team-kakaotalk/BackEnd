package com.emergency.challenge.chat.dto.request;

import com.emergency.challenge.chat.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto {

    private ChatMessage.MessageType type; // 메시지 타입

    private Long memberId;  //sender Id

    private String roomId; //ChatRoom Id

    private String name; //ChatRoomName

    private String message; //message

    private String sender; //message 발신자

}
