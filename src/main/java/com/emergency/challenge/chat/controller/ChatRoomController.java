package com.emergency.challenge.chat.controller;

import com.emergency.challenge.chat.model.ChatRoom;

import com.emergency.challenge.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping
/*MessageController를 통해서 Subscribe한테 message 전달*/
public class ChatRoomController {

    private final SimpMessageSendingOperations messagingTemplate;

    private final ChatRoomRepository chatRoomRepository;

    //1.채팅방 목록 상세 조회
    @GetMapping("/api/rooms/{roomId}")
    public ChatRoom roomInfo(@PathVariable String roomId) {

        return chatRoomRepository.findRoomById(roomId);
        //return chatRoomService.findRoomById(roomId);
    }

    //2.채팅방 목록 전체 조회
    @GetMapping("/api/rooms")
    public List<ChatRoom> getAllChatRooms(){

        return chatRoomRepository.findAllRoom();
    }

    //3.채팅방 생성
    @PostMapping("/api/rooms")
    public ChatRoom createRoom(@RequestBody String name) {

        return chatRoomRepository.createChatRoom(name);
    }

    //4.채팅방 이름 수정
    @PostMapping("/api/rooms/{roomId}")
    public boolean modifyRoom(@PathVariable String roomId, @RequestBody String name ){
        return chatRoomRepository.modifyChatRoom(roomId, name);
    }








}
