package com.emergency.challenge.chat.config;


import com.emergency.challenge.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

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
        * 2. webSocket connect reuqest
        * 3. if 'CONNECT' -> initial connection
        * */
        if (StompCommand.CONNECT == accessor.getCommand()){

            //intial connection => Token 유효성 검사
            //Access Token invalid => reissue
            //Refresh Token invalid => login again
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            String refreshToken = accessor.getFirstNativeHeader("Refresh_Token");
            tokenProvider.validateToken(accessToken);
            tokenProvider.validateToken(refreshToken);
            log.info("Authorization validity is {}",tokenProvider.validateToken(accessToken));
            log.info("RefreshToken validity is {}",tokenProvider.validateToken(refreshToken));

        }

        //initial connection은 되어 있고 메세지 주고 받기 전 채팅방을 구독하는 상태라면?
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()){

            //header destination = /sub/chats/rooms/{roomId}
            String destination = Optional.ofNullable((String)message.getHeaders().get("subDestination"))
                                .orElse("Invalid RoomId");
            log.info("message destination={}", destination);

            //Client마다 SessionID 생성 => ChatRoomId와 mapping
            String sessionId = (String) message.getHeaders().get("subSessionId");
        }


        return ChannelInterceptor.super.preSend(message, channel);
    }
}
