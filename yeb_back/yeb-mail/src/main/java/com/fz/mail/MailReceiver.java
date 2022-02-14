package com.fz.mail;

import com.fz.server.pojo.Employee;
import com.fz.server.pojo.MailConstants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/05/16:23
 * @Description: 消息接收者
 */
@Component
public class MailReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    private final JavaMailSender javaMailSender;

    private final MailProperties mailProperties;

    private final TemplateEngine engine;

    private final RedisTemplate redisTemplate;

    @Autowired
    public MailReceiver(JavaMailSender javaMailSender, MailProperties mailProperties, TemplateEngine engine, RedisTemplate redisTemplate) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
        this.engine = engine;
        this.redisTemplate = redisTemplate;
    }

//    @RabbitListener(queues = "mail.welcome")
//    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
//    public void handle(Employee employee){
//        //创建消息
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
//        try {
//            //发件人
//            mimeMessageHelper.setFrom(mailProperties.getUsername());
//            //收件人
//            mimeMessageHelper.setTo(employee.getEmail());
//            //发送主题
//            mimeMessageHelper.setSubject("入职欢迎邮件");
//            //发送日期
//            mimeMessageHelper.setSentDate(new Date());
//            //邮件内容
//            Context context = new Context();
//            context.setVariable("name",employee.getName());
//            context.setVariable("posName",employee.getPosition().getName());
//            context.setVariable("joblevelName",employee.getJoblevel().getName());
//            context.setVariable("departmentName",employee.getDepartment().getName());
//            String mail = engine.process("mail", context);
//            //设置html内容和格式
//            mimeMessageHelper.setText(mail,true);
//            //发送邮件
//            javaMailSender.send(mimeMessage);
//        } catch (MessagingException e) {
//            LOGGER.error("邮件发送失败===============》{}",e.getMessage());
//
//        }
//    }

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handle(Message message , Channel channel){
        Employee employee = (Employee)message.getPayload();
        MessageHeaders headers = message.getHeaders();
        //获取消息序号
        long tag = (long)headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("spring_returned_message_correlation");
        HashOperations  hashOperations = redisTemplate.opsForHash();

        try {
            //如果该消息id已经包含在Redis中，则不进行消息消费
            if(hashOperations.entries("mail_log").containsKey(msgId)){
                LOGGER.error("消息已经被消费了====================>{}",msgId);
                /**
                 * 手动确认消息
                 * tag:消息序号
                 * multiple:是否确认多条
                 */
                channel.basicAck(tag,false);
                return;
            }

            //创建消息
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            //发件人
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            //收件人
            mimeMessageHelper.setTo(employee.getEmail());
            //发送主题
            mimeMessageHelper.setSubject("入职欢迎邮件");
            //发送日期
            mimeMessageHelper.setSentDate(new Date());
            //邮件内容
            Context context = new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("posName",employee.getPosition().getName());
            context.setVariable("joblevelName",employee.getJoblevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());
            String mail = engine.process("mail", context);
            //设置html内容和格式
            mimeMessageHelper.setText(mail,true);
            //发送邮件
            javaMailSender.send(mimeMessage);
            LOGGER.info("邮件发送成功！");
            //将消息id存入Redis
            hashOperations.put("mail_log",msgId,"OK");
            //手动确认
            channel.basicAck(tag,false);
        } catch (Exception e) {
            /**
             * 手动确认消息
             * tag:消息序号
             * multiple:是否确认多条
             * requeue: 是否重回队列
             */

            try {
                channel.basicNack(tag,false,true);
            } catch (IOException ioException) {
                LOGGER.error("邮件发送失败===============》{}",e.getMessage());
            }
            LOGGER.error("邮件发送失败===============》{}",e.getMessage());

        }
    }
}
