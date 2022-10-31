package com.emergency.challenge.chat.model;



import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
@Builder
//extends Timestamped
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

//    @Id
    private String id;

//    @Column(nullable = false)
//    private String roomId;

//    @Column(nullable = false)
    private String name;

    //Many-To-Many => One-To-Many
//    @OneToMany(mappedBy = "chatRoom",
//            fetch = FetchType.LAZY,
//            orphanRemoval = true,
//            cascade = CascadeType.ALL)
//    List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

    //create ChatRoom
    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        //chatRoom Id Random으로 부여
        //chatRoom.id = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        chatRoom.id= UUID.randomUUID().toString();
        chatRoom.name = name;

        return chatRoom;
    }











}
