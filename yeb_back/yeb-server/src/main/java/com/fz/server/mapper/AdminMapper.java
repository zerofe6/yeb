package com.fz.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fz.server.pojo.Admin;
import org.apache.ibatis.annotations.Param;
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
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> getAllAdmin(@Param("id") Integer id, @Param("keyword") String keyword);
}
