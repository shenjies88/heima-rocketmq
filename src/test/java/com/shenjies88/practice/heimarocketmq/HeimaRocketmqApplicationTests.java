package com.shenjies88.practice.heimarocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class HeimaRocketmqApplicationTests {

	@Autowired
	private MsgProducer msgProducer;

	/**
	 * 发送同步消息
	 */
	@Test
	void sendSyncMsg() throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
		for (int i = 0; i < 10; i++) {
			Message message = new Message("test-topic", "sync", ("syncMsg " + i).getBytes(StandardCharsets.UTF_8));
			SendResult send = msgProducer.getProducer().send(message);
			SendStatus sendStatus = send.getSendStatus();
			log.info("同步发送状态 {}", sendStatus);
		}
	}

	/**
	 * 发送异步消息
	 */
	@Test
	void sendAsyncMsg() throws RemotingException, InterruptedException, MQClientException {
		for (int i = 0; i < 10; i++) {
			Message message = new Message("test-topic", "async", ("asyncMsg " + i).getBytes(StandardCharsets.UTF_8));
			msgProducer.getProducer().send(message, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
					log.info("异步发送状态 {}}", sendResult);
				}

				@Override
				public void onException(Throwable throwable) {
					log.error("异步发送失败", throwable);
				}
			});
		}
	}

	/**
	 * 发送单向消息
	 */
	@Test
	void sendOneWayMsg() throws RemotingException, InterruptedException, MQClientException {
		for (int i = 0; i < 10; i++) {
			Message message = new Message("test-topic", "oneWay", ("oneWayMsg " + i).getBytes(StandardCharsets.UTF_8));
			msgProducer.getProducer().sendOneway(message);
		}
		log.info("通向消息发送完成");
	}

	/**
	 * 简单消费
	 */
	@Test
	void simpleConsumer() throws MQClientException, InterruptedException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumerGroup");
		consumer.setNamesrvAddr("192.168.56.10:9876;192.168.56.10:9875");
		consumer.subscribe("test-topic", "*");
		consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
			for (MessageExt messageExt : list) {
				log.info("消费消息 {} 消息 {} 队列id {}", new String(messageExt.getBody()), messageExt.getMsgId(), messageExt.getQueueId());
			}
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		});
		consumer.start();
		TimeUnit.SECONDS.sleep(20);
	}

}
