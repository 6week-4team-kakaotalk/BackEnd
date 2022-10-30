package com.emergency.challenge.chat.model;


import com.emergency.challenge.domain.Member;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//Many-To-Many 중간 다리
public class ChatRoomMember implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Room_Id")
    private ChatRoom chatRoom;

    //FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Member_Id")
    private Member member;

    public ChatRoomMember(ChatRoom chatRoom, Member member){
        this.chatRoom = chatRoom.create();
        this.member = member;
    }


}
