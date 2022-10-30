package com.emergency.challenge.chat.service;


import com.emergency.challenge.chat.dto.request.ChatMessageRequestDto;
import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;


    public void save(ChatMessageRequestDto requestDto) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(requestDto.getMessage());
        chatMessage.setRoomId(requestDto.getRoomId());
        chatMessage.setType(requestDto.getType());
        chatMessage.setMemberId(requestDto.getMemberId());

        chatMessageRepository.save(chatMessage);

    }
}
