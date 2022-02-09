package com.fz.server.mapper;

import com.fz.server.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据用户id查询菜单列表 所有一级和二级菜单
     * @param id
     * @return
     */
    List<Menu> getMenuByAdminId(Integer id);

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
