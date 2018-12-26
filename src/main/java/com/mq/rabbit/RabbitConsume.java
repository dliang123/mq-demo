package com.mq.rabbit;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 16:51 2018/11/21
 */
@Slf4j
//@Component
public class RabbitConsume {


    @RabbitListener(queues = "test666_01", containerFactory = "rabbitListenerContainerFactory")
    private void test666_01Listener(Message message) {

        log.info("listener  message->{}", JSON.toJSONString(message));
        if (message != null) {
            log.info("listener body->{}", JSON.toJSONString(new String(message.getBody())));
        }

    }
}
