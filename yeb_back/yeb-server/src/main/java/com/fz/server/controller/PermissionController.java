package com.fz.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fz.server.pojo.Menu;
import com.fz.server.pojo.MenuRole;
import com.fz.server.pojo.RespBean;
import com.fz.server.pojo.Role;
import com.fz.server.service.IMenuRoleService;
import com.fz.server.service.IMenuService;
import com.fz.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: fanbo
 * @Date: 2021/12/22/9:02
 * @Description:权限组
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissionController {
    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation(value = "获取所有的角色信息")
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "添加一个角色")
    @PostMapping("/")
    public RespBean addRole(@RequestBody Role role){
        if(!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }
        if(roleService.save(role)){
            return RespBean.success("添加一个角色成功！");
        }
        return RespBean.error("添加角色失败");
    }

    @ApiOperation(value = "更新角色信息")
    @PutMapping("/")
    public RespBean updateRole(@RequestBody Role role){
        if(roleService.updateById(role)){
            return RespBean.success("更新角色信息成功！");
        }
        return  RespBean.error("更新角色信息失败！");
    }

    @ApiOperation(value = "删除角色信息")
    @DeleteMapping("/{id}")
    public RespBean deleteRole(@PathVariable Integer id){
        if(roleService.removeById(id)){
            return RespBean.success("删除角色信息成功！");
        }
        return RespBean.error("删除角色信息失败！");
    }

    @ApiOperation(value = "批量删除角色信息")
    @DeleteMapping("/")
    public  RespBean deleteRoles(Integer [] ids){
        if(roleService.removeByIds(Arrays.asList(ids))){
            return RespBean.success("批量删除角色信息成功！");
        }
        return RespBean.error("批量删除角色信息失败！");
    }

    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.getAllMenus();
    }

    @ApiOperation(value = "根据角色id查询菜单id")
    @GetMapping("/mid/{id}")
    public List<Integer> getMidByRid(@PathVariable Integer id){
//        return menuRoleService.getMidByRid(id);
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid",id)).stream().map(MenuRole::getMid)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "更新角色菜单")
    @PutMapping("/updaterolemenu")
    public RespBean updateRoleMenu(Integer rid,Integer[] mids){
        if(menuRoleService.updateRoleMenu(rid,mids)){
            return RespBean.success("更新角色菜单成功");
        }
        return RespBean.error("更新角色菜单失败");
    }

}
