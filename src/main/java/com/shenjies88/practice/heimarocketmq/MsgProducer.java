package com.shenjies88.practice.heimarocketmq;

import lombok.Getter;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 消息发送者
 *
 * @author shenjies88
 * @since 2021/6/26-11:48 上午
 */
@Getter
@Component
public class MsgProducer {

    private DefaultMQProducer producer;

    @PostConstruct
    public void init() throws MQClientException {
        //生产者组名
        producer = new DefaultMQProducer("producerGroup");
        //namesrv地址 分号分割
        producer.setNamesrvAddr("192.168.56.10:9876;192.168.56.10:9875");
        producer.start();
    }

    @PreDestroy
    public void destroy() {
        producer.shutdown();
    }

}
