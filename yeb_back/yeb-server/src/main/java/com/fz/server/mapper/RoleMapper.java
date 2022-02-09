package com.fz.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fz.server.pojo.Role;
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
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户id查询用户角色
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);
}
