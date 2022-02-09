package com.fz.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fz.server.pojo.Department;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
public interface IDepartmentService extends IService<Department> {

    void addDepartment(Department department);

    Integer deleteDepartment(Integer id);

    List<Department> getAllDepartment(Integer parentId);


}
