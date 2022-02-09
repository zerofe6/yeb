package com.fz.server.exception;

import com.fz.server.pojo.RespBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2021/12/21/16:30
 * @Description:全局异常处理
 */
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(SQLException.class)
    public RespBean mySqlException(SQLException exception){
        if(exception instanceof SQLIntegrityConstraintViolationException){
//            exception.printStackTrace();
            return RespBean.error("该数据有关联数据 操作失败！ ");
        }
        exception.printStackTrace();
        return RespBean.error("数据库异常，操作失败！");
    }

    @ExceptionHandler(IOException.class)
    public RespBean myIOException(IOException ioException){
        if(ioException instanceof IOException){
            ioException.printStackTrace();
            return RespBean.error("IO异常，操作失败");
        }
        return RespBean.error("IO异常！！！");
    }

}
