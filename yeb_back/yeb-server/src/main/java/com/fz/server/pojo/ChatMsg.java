package com.fz.server.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/12/9:55
 * @Description: 消息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ChatMsg {
    private String from;
    private String to;
    private String content;
    private LocalDateTime date;
    private String fromNickName;
}
