package com.emergency.challenge.chat.model;


import com.emergency.challenge.domain.Member;
import com.emergency.challenge.domain.Timestamped;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ChatRoom extends Timestamped implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomId;

    //Many-To-Many => One-To-Many
    @OneToMany(mappedBy = "chatRoom",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    //create ChatRoom
    public ChatRoom create(){
        ChatRoom chatRoom = new ChatRoom();
        //chatRoom Id Random으로 부여
        chatRoom.roomId = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        return chatRoom;
    }











}
