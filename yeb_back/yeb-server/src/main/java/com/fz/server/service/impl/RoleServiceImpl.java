package com.fz.server.service.impl;

import com.fz.server.pojo.Role;
import com.fz.server.mapper.RoleMapper;
import com.fz.server.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
