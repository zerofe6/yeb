package com.fz.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fz.server.pojo.Employee;
import com.fz.server.pojo.RespPageBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 查询员工
     * @param id
     * @return
     */


    List<Employee> getEmployee(Integer id);

    /**
     * 获取所有工资账套
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size);
}
