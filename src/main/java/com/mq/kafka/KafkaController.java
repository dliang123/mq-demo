package com.mq.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 20:09 2018/12/4
 */
@RestController
@RequestMapping(value = "kafka")
public class KafkaController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("send")
    public String send(String msg){
        kafkaTemplate.send("dongbotest", msg);
        return "success";
    }

    @RequestMapping("send2local")
    public String send2local(String msg){
        kafkaTemplate.send("kafkatest", msg);
        return "success";
    }

}
