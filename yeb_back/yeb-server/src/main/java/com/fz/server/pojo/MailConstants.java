package com.fz.server.pojo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/10/15:59
 * @Description: 消息状态
 */
public class MailConstants {
    //消息投递中
    public static final Integer DELIVERING = 0;

    //消息投递成功
    public static final Integer SUCCESS = 1;

    //消息投递失败
    public static final Integer FAILURE = 2;

    //最大重试次数
    public static final Integer MAX_TRY_CONTENT = 3;
    //消息超时时间
    public static final Integer  MSG_TIMEOUT = 1;

    //队列
    public static final String MAIL_QUEUE_NAME = "mail.queue";
    //交换机
    public static final String MAIL_EXCHANGE_NAME = "mail.exchange";
    //路由键
    public static final String MAIL_ROUTING_KEY_NAME = "mail.routing.key";

}
