package com.fz.server.controller;

import com.fz.server.pojo.Admin;
import com.fz.server.pojo.AdminLoginParam;
import com.fz.server.pojo.RespBean;
import com.fz.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录
 * @author Bo.Fan
 * @date 2021/9/10 16:25
 */
@RestController
public class LoginController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody  AdminLoginParam adminLoginParam, HttpServletRequest request){
//        System.out.println("接口拿到的用户名参数为:"+adminLoginParam.getUserName());
        return adminService.login(adminLoginParam.getUserName(),adminLoginParam.getPassword(),adminLoginParam.getCode(),request);
    }
    @ApiOperation("获取当前用户的信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal  principal){
        if(null == principal){
            return  null;
        }
        String userName = principal.getName();
        Admin admin = adminService.getAdminByUserName(userName);
        //保护用户信息 将密码设置为null
        admin.setPassword(null);
        // 后来加上 从数据将用户的角色拿到
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean lougut(){
        //前端拿到状态码200之后  前端删除token
        return  RespBean.success("注销成功");
    }
}
