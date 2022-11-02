package com.emergency.challenge.chat.config;


import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.model.ChatRoom;
import com.emergency.challenge.chat.redis.RedisSubscriber;
import com.emergency.challenge.chat.repository.ChatMessageRepository;
import com.emergency.challenge.chat.repository.ChatRoomRepository;
import com.emergency.challenge.chat.service.ChatRoomService;
import com.emergency.challenge.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    private final RedisSubscriber redisSubscriber;


    /*
    Invoked before the Message is actually send to the channel.
    This allows for modification of the message if necessary.
    If this method returns null, then the actual send the invocation will not occur
    */

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        //Create an instance from the payload and headers of the given Message.
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            Authentication authentication = tokenProvider.getAuthentication("Authorization");
            accessor.setUser(authentication);
        /*
        * 1. client -> server
        * 2. webSocket connect request
        * 3. if 'CONNECT' -> initial connection
        * */
        //if (StompCommand.CONNECT == accessor.getCommand()){
//        if (StompCommand.CONNECT == accessor.getCommand()) {
//            System.out.println("accessor = " + accessor);
            //intial connection => Token 유효성 검사
            //Access Token invalid => reissue
            //Refresh Token invalid => login again

            String accessToken = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(7);
            String refreshToken = accessor.getFirstNativeHeader("Refresh-Token");
            System.out.println("refreshToken = " + refreshToken);
            System.out.println("accessToken = " + accessToken);
            tokenProvider.validateToken(accessToken);
            tokenProvider.validateToken(refreshToken);
            log.info("Authorization validity is {}",tokenProvider.validateToken(accessToken));
            log.info("RefreshToken validity is {}",tokenProvider.validateToken(refreshToken));

        }

        //initial connection은 되어 있고 메세지 주고 받기 전 채팅방을 구독하는 상태라면?
//        else if (StompCommand.SUBSCRIBE == accessor.getCommand()){
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()){

            //header destination = /sub/chats/rooms/{roomId}
            String destination = Optional.ofNullable((String)message.getHeaders().get("simpDestination"))
                                .orElse("Invalid RoomId");
            log.info("message destination={}", destination);
            log.info("message header info {}", message.getHeaders());

            //roomId get
            String roomId = chatRoomService.getRoomId(destination);
            System.out.println("roomId 111111111111111111111111111111111111111111= " + roomId);
            //Client마다 SessionID 생성 => ChatRoomId와 mapping
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            log.info("session Id is {}", sessionId);
            chatRoomRepository.setUserEnterInfo(sessionId, roomId);

            //채팅방 인원수 +1
            chatRoomRepository.plusUserCount(roomId);
            String name = Optional.ofNullable((Principal) message.getHeaders()
                    .get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            System.out.println("message12312412412421412412421 = " + message);
            System.out.println(" simpleUser= " +message.getHeaders()
                    .get("simpUser"));
            System.out.println("name111111111111111111111111111111111111 = " + name);
            redisSubscriber.sendMessage(ChatMessage.builder()
                    .type(ChatMessage.MessageType.ENTER)
                    .roomId(roomId)
                    .sender(name)
                    .build());
            log.info("SUBSCRIBED {}, {}", name, roomId);
        }

        else if (StompCommand.DISCONNECT == accessor.getCommand()){
            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
            // 채팅방의 인원수를 -1한다.
            chatRoomRepository.minusUserCount(roomId);
            // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            redisSubscriber.sendMessage(ChatMessage.builder()
                    .type(ChatMessage.MessageType.QUIT)
                    .roomId(roomId)
                    .sender(name)
                    .build());
            // 퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
            chatRoomRepository.removeUserEnterInfo(sessionId);
            log.info("DISCONNECTED {}, {}", sessionId, roomId);
        }


        //return ChannelInterceptor.super.preSend(message, channel);
        return message;
    }
}
