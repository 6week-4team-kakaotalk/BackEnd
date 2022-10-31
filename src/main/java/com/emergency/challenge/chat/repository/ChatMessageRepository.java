package com.emergency.challenge.chat.repository;


import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
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
    private HashOperations<String, String, List<ChatMessage>> opsHashChatMessages;
    //private HashOperations<String, String, ChatMessage> opsHashChatMessages;

    @PostConstruct
    private void init(){
        opsHashChatMessages = redisTemplate.opsForHash();
    }


    public ChatMessage save(ChatMessage chatMessage) {
        System.out.println("chatMessage is + " + chatMessage);
//        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(String.class));
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
//        System.out.println("new Jackson2JsonRedisSerializer<>(String.class) = " + new Jackson2JsonRedisSerializer<>(String.class));

        hashOpsChatMessage.put(CHAT_MESSAGES, chatMessage.getRoomId(),chatMessage );

        //save messages
//        List<ChatMessage> chatMessages = opsHashChatMessages.get(CHAT_MESSAGES, chatMessage.getRoomId());
//        chatMessages.add(chatMessage);
//        opsHashChatMessages.put(CHAT_MESSAGES, chatMessage.getRoomId(),chatMessages);
        return null;
    }
}
