package com.fz.server.config;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fz.server.pojo.MailConstants;
import com.fz.server.pojo.MailLog;
import com.fz.server.service.IMailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/10/16:36
 * @Description: rabbitmq配置类
 */
@Configuration
public class RabbitMQConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    private IMailLogService mailLogService;

    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         * 确认消息回调，确认消息是否达到broker
         * data: 消息的唯一标识
         * ack: 确认结果
         * cause：失败的原因
         */
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            String msgId = data.getId();
            if(ack){
                LOGGER.info("{}===================>消息发送成功",msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",MailConstants.SUCCESS).eq("msgId",msgId));
            }else {
                LOGGER.error("{}===================>消息发送失败",msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status",MailConstants.FAILURE).eq("msgId",msgId));
            }
        });
        /**
         * 消息失败时，回调
         * msg:消息主题
         * repCode:响应码
         * repText:相应描述
         * exchange:交换机
         * routingkey:路由键
         */
        rabbitTemplate.setReturnCallback((msg,repCode,repText,exchange,routingkey)->{
            LOGGER.error("{}====================>消息发送至QUEUE时失败",msg.getBody());
        });
        return  rabbitTemplate;
    }

    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }
}
