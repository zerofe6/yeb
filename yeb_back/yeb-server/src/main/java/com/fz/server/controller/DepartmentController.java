package com.fz.server.controller;


import com.fz.server.pojo.Department;
import com.fz.server.pojo.RespBean;
import com.fz.server.service.IDepartmentService;
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
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/")
    public List<Department> getAllDepartment(){
        return departmentService.getAllDepartment(-1);
    }

    @ApiOperation(value = "添加一个部门")
    @PostMapping("/")
    public RespBean addDepartment(@RequestBody Department department){
        departmentService.addDepartment(department);
        return department.getResult()  ==1? RespBean.success("添加部门成功！",department): RespBean.error("添加部门失败");
    }

    @ApiOperation(value = "删除部门")
    @DeleteMapping("/{id}")
    public RespBean deleteDepartment(@PathVariable Integer id){
        Integer result = departmentService.deleteDepartment(id);
        if(result == -2){
            return RespBean.error("该部门下面还有子部门，删除失败");
        }
        else if(result == -1){
            return RespBean.error("该部门下面还有员工，删除失败");
        }else if(result == 1){
            return RespBean.success("删除部门成功");
        }else{
            return RespBean.error("删除部门失败");
        }
    }
}
