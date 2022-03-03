package com.fz.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象类
 * @author Bo.Fan
 * @date 2021/9/10 16:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object obj;

    /**
     * 成功返回结果
     * @param message
     * @return
     */
    public static RespBean success(String message){
        return new RespBean(200,message,null);
    }/**
     * 成功返回结果
     * @param message
     * @return
     */
    public static RespBean success(String message,Object obj){
        return new RespBean(200,message,obj);
    }
    /**
     * 失败返回
     * @param message
     * @return
     */
    public static  RespBean error(String message){
        return  new RespBean(500,message,null);
    }

    /**
     * 返回警告结果
     * @param message
     * @return
     */
    public static  RespBean warning(String message){
        return  new RespBean(222,message,null);
    }
    public static  RespBean error(String message,Object obj){
        return  new RespBean(500,message,obj);
    }
}
