package com.fz.mail;

import com.fz.server.pojo.MailConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/05/15:48
 * @Description:
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//启动时去掉serve连接数据库的部分
public class MailApplication {
    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class);
    }
//
//    @Bean
//    public Queue queue(){
//        return new Queue("mail.welcome");
//    }

    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }
}
