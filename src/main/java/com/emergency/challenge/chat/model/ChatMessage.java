package com.emergency.challenge.chat.model;


import com.emergency.challenge.chat.dto.request.ChatMessageRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor  //연관관계 사용 x
///extends Timestamped
public class ChatMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;  //message 작성자 Id

    @Column(nullable = false)
    private String roomId;   //message 속해있는 방 Id

    @Column(nullable = false)
    private String message; //message contents

    @Column(nullable = false)
    private String sender;  //message 발신자

    public enum MessageType{
        ENTER, TALK, QUIT   //입장, 채팅, 나가계
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    public ChatMessage(ChatMessageRequestDto requestDto){
        this.type = requestDto.getType();
        this.memberId = requestDto.getMemberId();
        this.roomId = requestDto.getRoomId();
        this.message = requestDto.getMessage();
        this.sender = requestDto.getSender();
    }


}
