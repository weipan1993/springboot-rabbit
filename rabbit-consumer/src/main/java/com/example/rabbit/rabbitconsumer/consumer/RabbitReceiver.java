package com.example.rabbit.rabbitconsumer.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Rabbit Receive
 *
 * @ClassName RabbitReceiver
 * @Author qinao
 * @Date 2019/7/2 17:01
 * @Version V1.0
 */
@Component
public class RabbitReceiver {

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}",
                durable="${spring.rabbitmq.listener.order.queue.durable}"),
            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
                durable="${spring.rabbitmq.listener.order.exchange.durable}",
                type= "${spring.rabbitmq.listener.order.exchange.type}",
                ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
        key = "${spring.rabbitmq.listener.order.key}")
    )
    @RabbitHandler
    public void onMessage(Channel channel, @Headers Map<String, Object> headers) throws Exception {
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        System.err.println("id:" + deliveryTag);
        channel.basicAck(deliveryTag, false);
    }
}
