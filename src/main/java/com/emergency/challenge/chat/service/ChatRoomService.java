package com.emergency.challenge.chat.service;


import com.emergency.challenge.chat.model.ChatMessage;
import com.emergency.challenge.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

        /**
         * destination정보에서 roomId 추출
         */
        public String getRoomId(String destination) {
            int lastIndex = destination.lastIndexOf('/');
            if (lastIndex != -1)
                return destination.substring(lastIndex + 1);
            else
                return "";
        }


    }


