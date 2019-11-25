package com.github.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 功能描述:  Redis发布订阅  消息监听类
 * @author: qinxuewu
 * @date: 2019/11/25 10:16
 * @since 1.0.0
 */

@Component
public class RedisReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisReceiver.class);
    public void receiveMessage(String message) {
        // TODO 这里是收到通道的消息之后执行的方法
        LOGGER.info("【Redis发布订阅 消息监听...】message={}",message);
    }
}
