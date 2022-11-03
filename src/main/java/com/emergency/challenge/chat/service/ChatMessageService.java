package com.emergency.challenge.chat.service;


import com.emergency.challenge.chat.dto.request.ChatMessageRequestDto;
import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.repository.ChatMessageRepository;
import com.emergency.challenge.controller.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChannelTopic channelTopic;

    public void save(ChatMessageRequestDto requestDto) {
        ChatMessage chatMessage = new ChatMessage();
        LocalDateTime localDateTime = LocalDateTime.now();
        chatMessage.setSender(requestDto.getSender());
        chatMessage.setMessage(requestDto.getMessage());
        chatMessage.setRoomId(requestDto.getRoomId());
        chatMessage.setType(requestDto.getType());
        chatMessage.setChatId(UUID.randomUUID().toString());
        chatMessage.setMemberId(requestDto.getMemberId());
        chatMessage.setCreatedAt(requestDto.getCreateAt());
        chatMessageRepository.save(chatMessage);
    }

    public void sendChatMessage(ChatMessage chatMessage) {
        if (ChatMessage.MessageType.ENTER.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에 입장했습니다.");
            chatMessage.setSender("[알림]");
        } else if (ChatMessage.MessageType.QUIT.equals(chatMessage.getType())) {
            chatMessage.setMessage(chatMessage.getSender() + "님이 방에서 나갔습니다.");
            chatMessage.setSender("[알림]");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
}
