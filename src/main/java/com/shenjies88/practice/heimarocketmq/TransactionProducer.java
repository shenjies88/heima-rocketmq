package com.shenjies88.practice.heimarocketmq;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author shenjies88
 * @since 2021/6/26-5:14 下午
 */
@Getter
@Slf4j
@Component
public class TransactionProducer {

    private TransactionMQProducer producer;

    @PostConstruct
    public void init() throws MQClientException {
        //生产者组名
        producer = new TransactionMQProducer("producerGroup");
        //namesrv地址 分号分割
        producer.setNamesrvAddr("192.168.56.10:9876;192.168.56.10:9875");
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                if ("TAG-A".equals(message.getTags())) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if ("TAG-B".equals(message.getTags())) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                } else {
                    return LocalTransactionState.UNKNOW;
                }
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                log.info("回查的消息 {}", messageExt);
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });
        producer.start();
    }

    @PreDestroy
    public void destroy() {
        producer.shutdown();
    }
}
