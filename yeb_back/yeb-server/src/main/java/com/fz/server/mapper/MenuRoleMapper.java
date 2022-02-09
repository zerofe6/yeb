package com.fz.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fz.server.pojo.MenuRole;
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
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    List<Integer> getMidByRid(Integer id);

    boolean updateRoleMenu(Integer rid, Integer[] mids);

    /**
     * 批量插入记录
     * @param rid
     * @param mids
     * @return
     */
    Integer insertRecord(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
