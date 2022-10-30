package com.emergency.challenge.chat.controller;


import com.emergency.challenge.controller.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
/*MessageController를 통해서 Subscribe한테 message 전달*/
public class ChatRoomController {

    private final SimpMessageSendingOperations messagingTemplate;





}
