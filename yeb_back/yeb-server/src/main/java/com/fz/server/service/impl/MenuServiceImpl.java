package com.fz.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fz.server.AdminUtils;
import com.fz.server.mapper.MenuMapper;
import com.fz.server.pojo.Menu;
import com.fz.server.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RedisTemplate<String ,Object> redisTemplate;
    /**
     * 根据用户id获取菜单列表
     * @return
     */
    @Override
    public List<Menu> getMenuByAdminId() {
//        Integer id = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
//        return menuMapper.getMenuByAdminId(id); 改用redis实现
        Integer id = AdminUtils.getCurrent().getId();
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        //从redis中获取menus
        List<Menu> menus = (List<Menu>)valueOperations.get("Menu_" + id);
        if(CollectionUtils.isEmpty(menus)){
            System.out.println("==========================================");
            System.out.println("根据用户id到redis中查询到的menus为空。写进redis");
            System.out.println("==========================================");
            menus = menuMapper.getMenuByAdminId(id);
            //将数据写进redis中
            valueOperations.set("Menu_"+id,menus);
        }
        return menus;
    }

    /**
     * 根据角色获取菜单
     * @return
     */
    @Override
    public List<Menu> getMenuByRoles() {
        return menuMapper.getMenuByRoles();
    }

    /**
     * 查询所有菜单
     * @return
     */
    @Override
    public List<Menu> getAllMenus(){
        return menuMapper.getAllMenus();
    }


}
