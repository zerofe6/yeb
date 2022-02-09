package com.fz.server.service.impl;

import com.fz.server.pojo.Department;
import com.fz.server.mapper.DepartmentMapper;
import com.fz.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public void addDepartment(Department department) {
        department.setEnabled(true);
        departmentMapper.addDepartment(department);
    }

    @Override
    public Integer deleteDepartment(Integer id) {
        Department department  = new Department();
        department.setId(id);
        departmentMapper.deleteDepartment(department);
//        return departmentMapper.deleteDepartment(id);
        return department.getResult();
    }

    @Override
    public List<Department> getAllDepartment(Integer parentId) {
        return departmentMapper.getAllDepartment(parentId);
    }

}
