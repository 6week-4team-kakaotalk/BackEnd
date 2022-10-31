package com.emergency.challenge.chat.controller;

import com.emergency.challenge.chat.model.ChatRoom;
import com.emergency.challenge.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@CrossOrigin
/*MessageController를 통해서 Subscribe한테 message 전달*/
public class ChatRoomController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;

    //방만들기
    @PostMapping("/rooms")
    @ResponseBody
    public ChatRoom createRoom(@RequestBody String name) {

        return chatRoomRepository.createChatRoom(name);
    }
    //방 하나 조회
    @GetMapping("/rooms/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {

        return chatRoomRepository.findRoomById(roomId);
    }


}
