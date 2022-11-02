package com.emergency.challenge.chat.redis;

import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

//    private final ObjectMapper objectMapper;
//    private final SimpMessageSendingOperations messagingTemplate;

    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate redisTemplate;


    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(ChatMessage chatMessage) {
        chatMessage.setUserCount(chatRoomRepository.getUserCount(chatMessage.getRoomId()));
        try {
            if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
                chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
                chatMessage.setSender("[알림]");
            } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
                chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
                chatMessage.setSender("[알림]");
            }
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
        redisTemplate.convertAndSend("sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        // 채팅방을 구독한 클라이언트에게 메시지 발송
        // route => sub/chat/room/{roomId}를 subscribe한 websocket client에게 메세지 전송
        //messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);

    }
}
