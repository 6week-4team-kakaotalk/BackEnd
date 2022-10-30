package com.emergency.challenge.chat.repository;


import com.emergency.challenge.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CHAT_MESSAGES = "CHAT_MESSAGES";

    private HashOperations<String, String, List<ChatMessage>> opsHashChatMessages;

    @PostConstruct
    private void init(){
        opsHashChatMessages = redisTemplate.opsForHash();
    }


    public ChatMessage save(ChatMessage chatMessage) {
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Long.class));
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));


//save messages
        List<ChatMessage> chatMessages = opsHashChatMessages.get(CHAT_MESSAGES, chatMessage.getRoomId());
        chatMessages.add(chatMessage);
        opsHashChatMessages.put(CHAT_MESSAGES, chatMessage.getRoomId(),chatMessages);
        return chatMessage;
    }
}
