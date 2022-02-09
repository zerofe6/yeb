package com.fz.server.controller;

import com.fz.server.pojo.Admin;
import com.fz.server.service.IAdminService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/12/10:07
 * @Description: 在线聊天
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private IAdminService adminService;


    @ApiModelProperty(value = "获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmins(String keyword){
        return adminService.getAllAdmin(keyword);
    }
}
