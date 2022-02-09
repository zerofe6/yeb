package com.fz.server.controller;


import com.fz.server.pojo.Joblevel;
import com.fz.server.pojo.RespBean;
import com.fz.server.service.IJoblevelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
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
@RequestMapping("/system/basic/joblevel")
public class JoblevelController {
    @Autowired
    private IJoblevelService joblevelService;

    @ApiOperation(value = "获取所有职称")
    @GetMapping("/")
    public List<Joblevel> getAllJobLevels(){
        return joblevelService.list();
    }

    @ApiOperation(value = "添加一个职称")
    @PostMapping("/")
    public RespBean addJobLevel(@RequestBody Joblevel joblevel){
        joblevel.setCreateDate(LocalDateTime.now());
        if(joblevelService.save(joblevel)){
            return RespBean.success("添加职称成功！");
        }
        return  RespBean.error("添加职称失败！");
    }

    @ApiOperation(value = "更新职称信息根据id")
    @PutMapping("/")
    public RespBean updateJobLevel(@RequestBody Joblevel joblevel){
        if(joblevelService.updateById(joblevel)){
            return RespBean.success("更新职称信息成功！");
        }
        return  RespBean.error("更新职称信息失败！");
    }

    @ApiOperation(value = "删除职称信息")
    @DeleteMapping("/{id}")
    public RespBean deleteJobLevel(@PathVariable Integer id){
        if(joblevelService.removeById(id)){
            return RespBean.success("删除职称信息成功！");
        }
        return RespBean.error("删除职称信息失败");
    }

    @ApiOperation(value = "批量删除职称信息")
    @DeleteMapping("/")
    public RespBean deletJoblevels( Integer[] ids ){
        if(joblevelService.removeByIds(Arrays.asList(ids))){
            return  RespBean.success("批量删除职称成功！");
        }
        return RespBean.error("批量删除职称失败！");
    }

}
