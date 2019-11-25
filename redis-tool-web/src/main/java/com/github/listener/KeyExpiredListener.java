package com.github.listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import java.nio.charset.StandardCharsets;

/**
 * 功能描述: 监听Redis过期KEY事件
 * @author: qinxuewu
 * @date: 2019/11/25 9:46
 * @since 1.0.0
 */
public class KeyExpiredListener extends KeyExpirationEventMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyExpiredListener.class);
    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel(),StandardCharsets.UTF_8);
        //过期的key
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        LOGGER.info("【redis key 过期事件监听】：pattern={},channel={},key={}",new String(pattern),channel,key);
    }
}
