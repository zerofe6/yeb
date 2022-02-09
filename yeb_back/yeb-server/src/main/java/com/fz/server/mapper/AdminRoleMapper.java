package com.fz.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fz.server.pojo.AdminRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@Repository
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    Integer addAdminRole(@Param("adminId") Integer adminId, Integer[] rids);
}
