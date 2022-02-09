package com.fz.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fz.server.pojo.Employee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
public interface EmployeeMapper extends BaseMapper<Employee> {

    /**
     * 获取所有员工分页
     * @param page
     * @param employee
     * @param beginDateScope
     * @return
     */
    IPage<Employee>  getEmployeePage(Page<Employee> page, @Param("employee") Employee employee, @Param("beginDateScope") LocalDate[] beginDateScope);

    String maxWorkID();

    List<Employee> getEmployee(Integer id);

    IPage getEmployeeWithSalary(Page<Employee> page);
}
