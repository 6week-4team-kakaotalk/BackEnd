package com.emergency.challenge.chat.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
/*MessageController를 통해서 Subscribe한테 message 전달*/
public class ChatRoomController {

    private final SimpMessageSendingOperations messagingTemplate;




}
