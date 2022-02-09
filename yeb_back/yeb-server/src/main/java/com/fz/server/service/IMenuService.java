package com.fz.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fz.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
public interface IMenuService extends IService<Menu> {
    /**
     * 根据用户的id查询菜单列表
     * @return
     */
    List<Menu> getMenuByAdminId();

    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenuByRoles();

    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> getAllMenus();


}
