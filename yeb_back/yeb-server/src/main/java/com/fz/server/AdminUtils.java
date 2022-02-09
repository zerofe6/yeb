package com.fz.server;

import com.fz.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2021/12/29/8:25
 * @Description: 操作员工具类
 */
public class AdminUtils {
    /**
     * 获取当前的登录操作员
     * @return
     */
    public static Admin getCurrent(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
