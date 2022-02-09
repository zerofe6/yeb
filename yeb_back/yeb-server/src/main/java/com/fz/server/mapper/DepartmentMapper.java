package com.fz.server.mapper;

import com.fz.server.pojo.Department;
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
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getAllDepartment(Integer parentId);

    void addDepartment(Department department);

    void deleteDepartment(Department id);

    List<Department> getAllDepartments();
}
