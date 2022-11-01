package com.emergency.challenge.chat.controller;

import com.emergency.challenge.chat.dto.request.ChatMessageRequestDto;
import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.repository.ChatMessageRepository;
import com.emergency.challenge.chat.service.ChatMessageService;
import com.emergency.challenge.controller.response.ResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {


    private final ChatMessageService chatMessageService;
    private final ChatMessageRepository chatMessageRepository;


    //route : pub/chat/message(SimpleAnnotation)
    @MessageMapping("/chat/message")
    public ResponseDto<?> message(ChatMessageRequestDto messageRequestDto){
        //======================================11/2 수정==============================
       if (ChatMessage.MessageType.TALK.equals(messageRequestDto.getType()))
            chatMessageService.save(messageRequestDto);
//    }

        return ResponseDto.success("Successfully saved");
    }

    //채팅방 메세지 전체 조회
    @GetMapping("/api/{roomId}/messages")
    @ResponseBody
    public List<ChatMessage> getMessages(@PathVariable String roomId) {

        return chatMessageRepository.ChatList(roomId);
    }

}
