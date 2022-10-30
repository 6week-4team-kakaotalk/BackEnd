package com.emergency.challenge.chat.redis;


import com.emergency.challenge.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;


/*chatRoom에 입장하여 메세지를 작성하면
* 해당 메세지를 Redis Topic에 발행하는 기능의 서비스이다.
* RedisPublisher를 통해 메세지를 발행하면
* 대기하고 있던 redis 구독 service가 메세지를 처리한다.
* */

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message){
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
