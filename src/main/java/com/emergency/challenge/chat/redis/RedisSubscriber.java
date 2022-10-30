package com.emergency.challenge.chat.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
//MessageListener을 상속받아 onMessage 메소드를 재작성한다.
public class RedisSubscriber implements MessageListener {

    /*Redis에서 message가 publish(발행되면)
    * 대기하고 있던 onMessage가
    * 해당 message를 받아 처리한다.
    * */
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    //onMessage 재작성
    @Override
    public void onMessage(Message message, byte[] pattern) {

        }

}
