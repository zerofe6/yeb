package com.fz.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fz.server.pojo.MenuRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
public interface IMenuRoleService extends IService<MenuRole> {

    List<Integer> getMidByRid(Integer id);

    /**
     * 更新角色菜单
     * @param rid 角色id
     * @param mids 菜单id数组
     * @return
     */
    boolean updateRoleMenu(Integer rid, Integer[] mids);
}
