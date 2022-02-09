package com.fz.server.controller;


import com.fz.server.pojo.Admin;
import com.fz.server.pojo.RespBean;
import com.fz.server.pojo.Role;
import com.fz.server.service.IAdminService;
import com.fz.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/")
    public List<Admin> getAllAdmin(String keyword){
        return adminService.getAllAdmin(keyword);
    }

    @ApiOperation(value = "更新操作员")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin){
        if(adminService.updateById(admin)){
            return RespBean.success("更新操作员成功！");
        }
        return RespBean.error("更新操作员失败！");
    }

    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id){
        if(adminService.removeById(id)){
            return RespBean.success("删除操作员成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/roles")
    public List<Role> getAllRoles(){
        return roleService.list();
    }
    @ApiOperation(value = "更新操作员权限")
    @PutMapping("/role")
    public RespBean updateAdminRole(Integer adminId ,Integer[] rids){
        return adminService.updateAdminRole(adminId,rids);
    }



}
