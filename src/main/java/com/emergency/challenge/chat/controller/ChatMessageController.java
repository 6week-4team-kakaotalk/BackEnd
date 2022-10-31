package com.emergency.challenge.chat.controller;

import com.emergency.challenge.chat.dto.request.ChatMessageRequestDto;
import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {


    private final ChatMessageService chatMessageService;


    //route : pub/chat/message(SimpleAnnotation)
    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto messageRequestDto){
//        if (ChatMessage.MessageType.ENTER.equals(messageRequestDto.getType())){
            chatMessageService.save(messageRequestDto);
//    }

//        @MessageMapping("/ws-stomp")
//        @SendTo("/sub/messages")
//        public String send(String username){
//            return "Hello, " + username;



    }




}
