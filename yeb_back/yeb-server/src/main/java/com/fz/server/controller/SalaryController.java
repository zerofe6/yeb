package com.fz.server.controller;


import com.fz.server.pojo.RespBean;
import com.fz.server.pojo.Salary;
import com.fz.server.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;

    @ApiOperation(value = "获取所有工资账套")
    @GetMapping("/")
    public List<Salary> getAllSalaries(){
        return salaryService.list();
    }
    @ApiOperation(value = "添加工资账套")
    @PostMapping("/")
    public RespBean addSalaries(@RequestBody Salary salary){
        return salaryService.save(salary)?RespBean.success("添加工资账套成功！"):RespBean.error("添加工资账套失败");
    }
    @ApiOperation(value = "删除工资账套")
    @DeleteMapping("/{id}")
    public RespBean deleteSalaries(@RequestParam Integer id){
        return salaryService.removeById(id)?RespBean.success("删除工资账套成功！"):RespBean.error("删除工资账套失败");
    }
    @ApiOperation(value = "更新工资账套")
    @PutMapping("/")
    public RespBean updateSalaries(@RequestBody Salary salary){
        return salaryService.updateById(salary)?RespBean.success("更新工资账套成功！"):RespBean.error("更新工资账套失败");
    }

}
