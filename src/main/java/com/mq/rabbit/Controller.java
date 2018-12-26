package com.mq.rabbit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @Description
 * @Author dengliang
 * @Email liang.deng@atzuche.cn
 * @Date Created in 10:20 2018/11/21
 */
@RestController()
@RequestMapping(value = "rabbit")
public class Controller {

    @Autowired
    private RabbitService rabbitService;

    @PostMapping(value = "push")
    public String pushMessage() {

        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            rabbitService.sendMessage(random.nextInt(100));
        }

        return "success";
    }
}
