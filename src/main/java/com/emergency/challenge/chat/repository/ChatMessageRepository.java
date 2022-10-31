package com.emergency.challenge.chat.repository;


import com.emergency.challenge.chat.dto.response.ChatMessageResponseDto;
import com.emergency.challenge.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatMessageRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CHAT_MESSAGES = "CHAT_MESSAGES";

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatMessage> hashOpsChatMessage;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, ChatMessage> valueOps;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, List<ChatMessage>> opsHashChatMessages;
    //private HashOperations<String, String, ChatMessage> opsHashChatMessages;

    @PostConstruct
    private void init(){
        opsHashChatMessages = redisTemplate.opsForHash();
    }

    public List<ChatMessage> ChatList(String roomId){

        List<ChatMessage> chatMessages = hashOpsChatMessage.values(roomId);
        log.info("뭐가 나오나 보자 {} ",chatMessages);
        return chatMessages;
    }

    public ChatMessageResponseDto save(ChatMessage chatMessage) {
        System.out.println("chatMessage is + " + chatMessage);
//        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(String.class));
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
//        System.out.println("new Jackson2JsonRedisSerializer<>(String.class) = " + new Jackson2JsonRedisSerializer<>(String.class));

//        hashOpsChatMessage.put(CHAT_MESSAGES, chatMessage.getRoomId(),ChatMessage.builder()
//                        .message(chatMessage.getMessage())
//                        .roomId(chatMessage.getRoomId())
//                        .type(chatMessage.getType())
//                        .sender(chatMessage.getSender())
//                        .id(chatMessage.getId())
//                .build());
        hashOpsChatMessage.put(chatMessage.getRoomId(), chatMessage.getChatId(),chatMessage);
//        valueOps.append(CHAT_MESSAGES, chatMessage.toString());
        //save messages
//        List<ChatMessage> chatMessages = opsHashChatMessages.get(CHAT_MESSAGES, chatMessage.getRoomId());
//        chatMessages.add(chatMessage);
//        opsHashChatMessages.put(CHAT_MESSAGES, chatMessage.getRoomId(),chatMessages);




        return  ChatMessageResponseDto.builder()
                .message(chatMessage.getMessage())
                .roomId(chatMessage.getRoomId())
                .memberId(chatMessage.getMemberId())
                .name(chatMessage.getSender())
                .build();
    }
}
