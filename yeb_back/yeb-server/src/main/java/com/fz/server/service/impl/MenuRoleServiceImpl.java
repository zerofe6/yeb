package com.fz.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fz.server.pojo.MenuRole;
import com.fz.server.mapper.MenuRoleMapper;
import com.fz.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Override
    public List<Integer> getMidByRid(Integer id) {
        return menuRoleMapper.getMidByRid(id);
    }

    @Override
    @Transactional //事务的注解
    public boolean updateRoleMenu(Integer rid, Integer[] mids) {
        menuRoleMapper.delete(new UpdateWrapper<MenuRole>().eq("rid",rid));
        if(null==mids||0==mids.length){
            return true;
        }
        return menuRoleMapper.insertRecord(rid,mids)>0? true:false;
        //        return menuRoleMapper.updateRoleMenu(rid,mids);
    }
}
