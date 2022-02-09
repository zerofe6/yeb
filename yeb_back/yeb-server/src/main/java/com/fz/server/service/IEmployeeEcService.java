package com.fz.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fz.server.pojo.Employee;
import com.fz.server.pojo.EmployeeEc;
import com.fz.server.pojo.RespBean;
import com.fz.server.pojo.RespPageBean;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
public interface IEmployeeEcService extends IService<EmployeeEc> {

    RespPageBean getEmployeePage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    RespBean maxWorkID();

    boolean addEmployee(Employee employee);
}
