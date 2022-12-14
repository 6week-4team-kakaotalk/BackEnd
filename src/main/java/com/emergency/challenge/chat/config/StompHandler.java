package com.emergency.challenge.chat.config;

import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.repository.ChatRoomRepository;
import com.emergency.challenge.chat.service.ChatMessageService;
import com.emergency.challenge.chat.service.ChatRoomService;
import com.emergency.challenge.domain.Member;
import com.emergency.challenge.domain.RefreshToken;
import com.emergency.challenge.domain.UserDetailsImpl;
import com.emergency.challenge.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
//    private final RedisSubscriber redisSubscriber;


    /*
    Invoked before the Message is actually send to the channel.
    This allows for modification of the message if necessary.
    If this method returns null, then the actual send the invocation will not occur
    */

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        //Create an instance from the payload and headers of the given Message.
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        /*
        * 1. client -> server
        * 2. webSocket connect request
        * 3. if 'CONNECT' -> initial connection
        * */
        //if (StompCommand.CONNECT == accessor.getCommand()){
        if (StompCommand.CONNECT == accessor.getCommand()) {
            System.out.println("accessor = " + accessor);
            //intial connection => Token ????????? ??????
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
//            accessor.setUser(new User(accessor.getLogin());
        }

        //initial connection??? ?????? ?????? ????????? ?????? ?????? ??? ???????????? ???????????? ?????????????
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
            //Client?????? SessionID ?????? => ChatRoomId??? mapping
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            log.info("session Id is {}", sessionId);
            chatRoomRepository.setUserEnterInfo(sessionId, roomId);

            //????????? ????????? +1
            chatRoomRepository.plusUserCount(roomId);
//            String name = Optional.ofNullable((Principal) message.getHeaders()
//                    .get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            Authentication authentication = tokenProvider.getAuthentication(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")));

            log.info("authentication is {}",authentication);
            Member member = ((UserDetailsImpl) authentication.getPrincipal()).getMember();
            //?????? ?????? ???????????? ????????? ????????????
//            Member member = tokenProvider.getMemberFromAuthentication();
            log.info("member INFO {}",member);
            System.out.println("message12312412412421412412421 = " + message);
            System.out.println(" simpleUser= " +message.getHeaders()
                    .get("simpUser"));
//            System.out.println("name111111111111111111111111111111111111 = " + member.getNickName());

            chatMessageService.sendChatMessage(ChatMessage.builder()
                    .type(ChatMessage.MessageType.ENTER)
                    .roomId(roomId)
                    .sender(member.getNickName())
                    .build());

            log.info("SUBSCRIBED {}, {}", member.getNickName(), roomId);
        }

        else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()){
            // ????????? ????????? ??????????????? sesssionId??? ????????? id??? ?????????.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);

            Authentication authentication = tokenProvider.getAuthentication(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")));

            log.info("authentication is {}",authentication);
            Member member = ((UserDetailsImpl) authentication.getPrincipal()).getMember();
            // ???????????? ???????????? -1??????.
            chatRoomRepository.minusUserCount(roomId);
            // ??????????????? ?????? ???????????? ???????????? ????????????.(redis publish)
//            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            chatMessageService.sendChatMessage(ChatMessage.builder()
                    .type(ChatMessage.MessageType.QUIT)
                    .roomId(roomId)
                    .sender(member.getNickName())
                    .build());
            // ????????? ?????????????????? roomId ?????? ????????? ????????????.
            chatRoomRepository.removeUserEnterInfo(sessionId);
            log.info("DISCONNECTED {}, {}", sessionId, roomId);
        }


        //return ChannelInterceptor.super.preSend(message, channel);
        return message;
    }
}
