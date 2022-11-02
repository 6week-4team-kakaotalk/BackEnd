package com.emergency.challenge.chat.model;


import com.emergency.challenge.chat.dto.request.ChatMessageRequestDto;
import com.emergency.challenge.domain.Timestamped;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//연관관계 사용 x
///extends Timestamped
public class ChatMessage extends Timestamped implements Serializable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;

//    @Column(nullable = false)
    private Long memberId;  //message 작성자 Id

//    @Column(nullable = false)
    private String ChatId;

//    @Column(nullable = false)
    private String roomId;   //message 속해있는 방 Id

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

//    @Column(nullable = false)
    private String message; //message contents

//    @Column(nullable = false)
    private String sender;  //message 발신자

    public enum MessageType{
        ENTER, TALK, QUIT   //입장, 채팅, 나가계
    }

    //@Enumerated(EnumType.STRING)
    //@Column(nullable = false)
    private MessageType type;

    //채팅방 인원수, 채팅방 내에서 메세지가 전달될 때 인원수 갱신시 사용
    private long userCount;

    public ChatMessage(ChatMessageRequestDto requestDto, long userCount){
        this.type = requestDto.getType();
        this.memberId = requestDto.getMemberId();
        this.roomId = requestDto.getRoomId();
        this.message = requestDto.getMessage();
        this.sender = requestDto.getSender();
        this.userCount = userCount;

    }


}
