package com.fz.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fz.server.pojo.Employee;
import com.fz.server.pojo.MailConstants;
import com.fz.server.pojo.MailLog;
import com.fz.server.service.IMailLogService;
import com.fz.server.service.impl.EmployeeServiceImpl;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/11/8:39
 * @Description: 邮件发送定时任务
 */
@Component
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 邮件发送定时任务，10s执行一次
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void MailTask(){
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>().eq("status", 0).lt("tryTime", LocalDateTime.now()));
        list.forEach(mailLog -> {
            //如果重试次数超过3次，更新状态为投递失败，不再重试
            if(3<=mailLog.getCount()){
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", MailConstants.FAILURE).eq("msgId",mailLog.getMsgId()));
            }
            mailLogService.update(new UpdateWrapper<MailLog>().
                    set("count",mailLog.getCount()+1)
                    .set("updateTime",LocalDateTime.now())
                    .set("tryTime",LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT)));
            Employee employee = employeeService.getEmployee(mailLog.getEid()).get(0);
            //发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_NAME,employee,new CorrelationData(mailLog.getMsgId()));
        });

    }
}