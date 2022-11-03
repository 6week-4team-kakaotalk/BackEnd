package com.emergency.challenge.chat.controller;

import com.emergency.challenge.chat.dto.request.ChatMessageRequestDto;
import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.model.ChatRoom;
import com.emergency.challenge.chat.redis.RedisSubscriber;
import com.emergency.challenge.chat.repository.ChatMessageRepository;
import com.emergency.challenge.chat.repository.ChatRoomRepository;
import com.emergency.challenge.chat.service.ChatMessageService;
import com.emergency.challenge.controller.response.ResponseDto;
import com.emergency.challenge.domain.Member;
import com.emergency.challenge.error.ErrorCode;
import com.emergency.challenge.repository.MemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {


    private final ChatMessageService chatMessageService;
    private final ChatMessageRepository chatMessageRepository;
    //private final ChatMessage chatMessage;

    //route : pub/chat/message(SimpleAnnotation)
    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto messageRequestDto, ChatMessage chatMessage){
        //======================================11/2 수정==============================
           chatMessageService.save(messageRequestDto);
           log.info("chatMessage type is {}", messageRequestDto.getType());
        ChatMessage message = ChatMessage.builder()
                .type(messageRequestDto.getType())
                .roomId(messageRequestDto.getRoomId())
                .sender(messageRequestDto.getSender())
                .message(messageRequestDto.getMessage())
                .memberId(messageRequestDto.getMemberId())
                .createdAt(chatMessage.getCreatedAt())
                .modifiedAt(chatMessage.getModifiedAt())
                .build();
        chatMessageService.sendChatMessage(message);


       log.info("메세지 리시브 {}", "완료");
    }

    //채팅방 메세지 전체 조회
    @GetMapping("/api/{roomId}/messages")
    @ResponseBody
    public List<ChatMessage> getMessages(@PathVariable String roomId) {

        return chatMessageRepository.ChatList(roomId);
    }

}
