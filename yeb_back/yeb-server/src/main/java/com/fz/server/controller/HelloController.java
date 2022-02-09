package com.fz.server.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Bo.Fan
 * @date 2021/9/13 14:06
 */
@RestController
public class HelloController {
   @GetMapping("hello")
   public String hello(){
       return "hello";
   }

    @ApiOperation(value = "基本资料admin用户有权访问，非登陆用户无权限访问")
    @GetMapping("/employee/basic/**")
    public String test(){
       return "基本资料";
   }
   @ApiOperation(value = "高级资料admin用户无法访问，没有权限")
   @GetMapping("/employee/advanced/**")
    public String test1(){
       return "高级资料";
   }
}
