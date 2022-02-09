package com.fz.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/04/8:39
 * @Description: 分页公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespPageBean {
    /**
     *分页的总条目数
     */
    private Long total;
    /**
    *数据
     */
    private List<?> data;
}
