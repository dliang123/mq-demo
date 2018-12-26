package com.mq.rabbit.transaction;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConfirmMode {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void testSyncMode() {
        // 获取连接工厂
        ConnectionFactory connectionFactory =
                rabbitTemplate.getConnectionFactory();

        // 开启连接 - tcp连接
        Connection connection = connectionFactory.createConnection();

        // 建立信道 构造参数 true代表该信道开启 Transactional 事务模式, false 代表为非事务模式
        Channel channel = connection.createChannel(false);

        long start = System.currentTimeMillis();
        for (int i = 0; i <= 10; i++) {
            try {
                // 开启发布确认模式
                channel.confirmSelect();

                channel.basicPublish("test666", "test66", true, MessageProperties.PERSISTENT_BASIC, ("第" + (i + 1) + "条消息").getBytes());

                // 阻塞方法，直到mq服务器确认消息
                if (channel.waitForConfirms()) {
                    log.info("消息发送成功");
                }
            } catch (Exception e) {
                // 发生异常，说明消息没有到达broker的queue中，回滚。
                log.error("mq broker error...");
            }
        }
        System.out.println("发送确认 - 同步确认提交下，10000条消息发送共耗时: " + (System.currentTimeMillis() - start) + "ms");

    }

    public void testAsyncMode() {
        // 开启confirm模式, 模拟发送一千条消息，记录总耗时
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationIdString();
            log.debug("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });

        // 获取连接工厂
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            System.out.println("=====消息确认回调了======");
            if (ack) {
                System.out.println("消息id为: " + correlationData + "的消息，已经被ack成功");
            } else {
                System.out.println("消息id为: " + correlationData + "的消息，消息nack，失败原因是：" + cause);
            }
        });

        // 开启连接 - tcp连接
        // 准备发送一万条测试消息
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("x-hello", "test", ("第" + (i + 1) + "条消息").getBytes(), new CorrelationData(String.valueOf(i)));
        }
        System.out.println("消息确认 - 异步确认，1000条消息发送共耗时: " + (System.currentTimeMillis() - start) + "ms");
    }

}
