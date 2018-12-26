package com.mq.rabbit;

import com.mq.rabbit.transaction.TransactionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private TransactionMode transactionMode;

    @PostMapping(value = "push")
    public String pushMessage() {

        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            rabbitService.sendMessage(random.nextInt(100));
        }

        return "success";
    }

    @GetMapping(value = "testTransaction")
    public void testTransaction() {

        transactionMode.testTransactionalMode();
    }
}
