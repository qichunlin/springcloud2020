package com.qcl.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author chunlin.qi@hand-china.com
 * @version 1.0
 * @EnableBinding(Sink.class) 消费者使用Sink
 * @description
 * @date 2020/8/12
 */
@Component
@EnableBinding(Sink.class)
public class ReceiveMessageListenerController {

    @Value("${server.port}")
    private String serverPort;


    @StreamListener(Sink.INPUT)
    public void input(Message<String> message) {
        System.out.println("消费者2号,----->接受到的消息: " + message.getPayload() + "\t  port: " + serverPort);
    }
}
