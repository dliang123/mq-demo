package com.mq.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 20:11 2018/12/4
 */
@Component
public class TestConsumer {

    @KafkaListener(topics = "dongbotest")
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        System.out.printf("topic = %s, offset = %d, value = %s \n", record.topic(), record.offset(), record.value());
    }

    @KafkaListener(topics = "dongbotest")
    public void listen2 (String message) throws Exception {
        System.out.printf(" message = %s \n", message);
    }

    @KafkaListener(topics = "kafkatest")
    public void kafkatestListener (String message) throws Exception {
        System.out.printf(" message = %s \n", message);
    }

}
