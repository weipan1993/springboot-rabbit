package com.example.rabbit.rabbitproducer.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Rabbit Sender
 *
 * @ClassName RabbitSender
 * @Author qinao
 * @Date 2019/7/2 16:10
 * @Version V1.0
 */
@Component
public class RabbitSender {

    private  static Logger log = LoggerFactory.getLogger(RabbitSender.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            log.info("correlationData: " + correlationData);
            log.info("ack：" + ack);
            if (!ack) {
                log.info("异常处理...");
            }
        }
    };

    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(Message message, int i, String s, String s1, String s2) {
            log.info(message.toString(), i, s, s1, s2);
        }
    };

    public void sendMessage() {
        rabbitTemplate.setReturnCallback(returnCallback);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend("exchange", "key", "OK", correlationData);
    }

}
