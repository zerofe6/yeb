package com.fz.server.controller;

import com.fz.server.FastDFSUtils;
import com.fz.server.pojo.Admin;
import com.fz.server.pojo.RespBean;
import com.fz.server.service.IAdminService;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2022/01/12/10:47
 * @Description:
 */
@RestController
public class AdminInfoController {
    @Autowired
    private IAdminService adminService;

    @ApiModelProperty(value = "更新当前用户的信息")
    @PutMapping("/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication){
        if(adminService.updateById(admin)){
            //更新authentication
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin
            ,null,authentication.getAuthorities()));
            return RespBean.success("更新用户信息成功！");
        }
        return RespBean.error("更新用户信息失败！");
    }

    @ApiModelProperty(value = "更新用户密码")
    @PutMapping("/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String,Object> info){
        String oldPass = (String) info.get("oldPas");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updateAdminPassword(oldPass,pass,adminId);

    }

    @ApiModelProperty(value = "更新用户头像")
    @PutMapping("/admin/userface")
    public RespBean updateAdminUserFace(MultipartFile file,Integer id,Authentication authentication){
        String[] filePath = FastDFSUtils.upload(file);
        String url = FastDFSUtils.getTrackerUrl()+filePath[0]+"/"+filePath[1];
        if(id == null){
            return RespBean.warning("修改用户头像的用户id不能为空！！！");
        }
        System.out.println("=======================================");
        System.out.println("上传文件后拿到的URL 为："+url);
        System.out.println("=======================================");
        return adminService.updateAdminUserFace(url,id,authentication);
    }
}
