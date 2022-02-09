package com.fz.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fz.server.mapper.EmployeeMapper;
import com.fz.server.pojo.Employee;
import com.fz.server.pojo.EmployeeEc;
import com.fz.server.mapper.EmployeeEcMapper;
import com.fz.server.pojo.RespBean;
import com.fz.server.pojo.RespPageBean;
import com.fz.server.service.IEmployeeEcService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@Service
public class EmployeeEcServiceImpl extends ServiceImpl<EmployeeEcMapper, EmployeeEc> implements IEmployeeEcService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 获取所有员工分页
     * @param currentPage
     * @param size
     * @param employee
     * @param beginDateScope
     * @return
     */
    @Override
    public RespPageBean getEmployeePage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope) {
        //开启分页
        Page<Employee> page = new Page<>(currentPage,size);
        IPage<Employee> employeePage = employeeMapper.getEmployeePage(page, employee, beginDateScope);
        RespPageBean respPageBean = new RespPageBean(employeePage.getTotal(),employeePage.getRecords());
        return respPageBean;
    }

    @Override
    public RespBean maxWorkID() {
        String maxWorkID = employeeMapper.maxWorkID();
        return RespBean.success("获取工号成功！",maxWorkID);
    }

    @Override
    public boolean addEmployee(Employee employee) {
        return employeeMapper.insert(employee)==1? true:false;
    }


}
