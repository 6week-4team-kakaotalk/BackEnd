package com.emergency.challenge.chat.service;


import com.emergency.challenge.chat.dto.request.ChatMessageRequestDto;
import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.repository.ChatMessageRepository;
import com.emergency.challenge.controller.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;


    public void save(ChatMessageRequestDto requestDto) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(requestDto.getSender());
        chatMessage.setMessage(requestDto.getMessage());
        chatMessage.setRoomId(requestDto.getRoomId());
        chatMessage.setType(requestDto.getType());
        chatMessage.setChatId(UUID.randomUUID().toString());
        chatMessage.setMemberId(requestDto.getMemberId());

        chatMessageRepository.save(chatMessage);
    }
}
