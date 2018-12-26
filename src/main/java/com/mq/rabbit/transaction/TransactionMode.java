package com.mq.rabbit.transaction;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TransactionMode {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void testTransactionalMode() {
        // 开启事务模式，模拟发送一万条消息，记录总耗时
        // 获取连接工厂
        ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();

        // 开启连接 - tcp连接
        Connection connection = connectionFactory.createConnection();

        // 建立信道 构造参数 true代表该信道开启 Transactional 事务模式
        // 此处传入true，就不需要再显示编码 channel.txSelect()了
        // 内部已经调用了channle.txSelect();
        Channel channel = connection.createChannel(true);

        // 准备发送一万条测试消息, 每条消息都会开启一个新的事务。
        long start = System.currentTimeMillis();
        for (int i = 0; i <= 10; i++) {
            try {
                // channel.txSelect();
                channel.basicPublish("test666", "test66", true, MessageProperties.TEXT_PLAIN, ("第" + (i + 1) + "条消息").getBytes());

//                if(i==7){
//                    throw  new Exception("ee");
//                }

                channel.basicPublish("test666", "test66", true, MessageProperties.TEXT_PLAIN, ("第" + (i + 1) + "条消息 plus").getBytes());
                channel.txCommit();
            } catch (Exception e) {
                // 发生异常，说明消息没有到达broker的queue中，回滚。
                try {
                    System.out.println("提交事务失败，事务回滚 i = " + i);
                    channel.txRollback();
                } catch (IOException e1) {
                    System.out.println("mq broker error...");
                }
                System.out.println("mq broker error...");
            }
        }
        System.out.println("事务方式单消息单事务提交下，10000条消息发送共耗时: " + (System.currentTimeMillis() - start) + "ms");

    }
}
