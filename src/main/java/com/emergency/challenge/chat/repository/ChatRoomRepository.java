package com.emergency.challenge.chat.repository;

import com.emergency.challenge.chat.dto.request.RequestRoomDto;
import com.emergency.challenge.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository{

    private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsChatRoomUpdate;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    // 모든 채팅방 조회 찾는거
    public List<ChatRoom> findAllRoom() {
        return hashOpsChatRoom.values(CHAT_ROOMS);
    }
    // 특정 채팅방 조회
    public ChatRoom findRoomById(String id) {
        return hashOpsChatRoom.get(CHAT_ROOMS, id);
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public ChatRoom createChatRoom(RequestRoomDto name) {
        System.out.println("name is :" + name);
        ChatRoom chatRoom = ChatRoom.create(name);
        System.out.println("chatRoom is :" + chatRoom.getName());
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getId(), chatRoom);
        System.out.println("chatId is :" + chatRoom.getId());
        return ChatRoom.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .build();
    }

    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, String roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }
    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // 채팅방 유저수 조회
    public long getUserCount(String roomId) {
        return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
    }


    //채팅방 이름 수정
    public ChatRoom modifyChatRoom(String roomId, String name) {
//        hashOpsChatRoomUpdate.put("CHAT_ROOM", roomId, name);
//        return true;
        //System.out.println("name is :" + name);
        //ChatRoom chatRoom = new ChatRoom();
        //System.out.println("chatRoom is :" + chatRoom.getName());
        //hashOpsChatRoom.put(CHAT_ROOMS, roomId, chatRoom);
        //System.out.println("chatId is :" + chatRoom.getId());
        hashOpsChatRoom.put(CHAT_ROOMS, roomId, ChatRoom.builder()
                .id(roomId)
                .name(name)
                .build());

        //======================11/2===========================
        //boolean type -> ChatRoom으로 수정
        return hashOpsChatRoom.get(CHAT_ROOMS, roomId);
    }
}