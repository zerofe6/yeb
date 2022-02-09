package com.fz.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.fz.server.mapper.EmployeeMapper;
import com.fz.server.mapper.MailLogMapper;
import com.fz.server.pojo.*;
import com.fz.server.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeEcService employeeEcService;

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IPositionService positionService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IPoliticsStatusService politicsStatusService;

    @Autowired
    private INationService nationService;

    @Autowired
    private IJoblevelService joblevelService;

    private ServletOutputStream outputStream;


    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MailLogMapper mailLogMapper;

    @ApiOperation(value = "分页查询所有的员工")
    @PostMapping("/")
    public RespPageBean getAllEmployee(@RequestParam(defaultValue = "1") Integer  currentPage,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       Employee employee, LocalDate[] beginDateScope){
        return employeeEcService.getEmployeePage(currentPage,size,employee,beginDateScope);

    }

    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/politics")
    public List<PoliticsStatus> getAllPoliticsStatus(){
        return politicsStatusService.list();
    }

    @ApiOperation(value = "获取所有民族")
    @GetMapping("/nation")
    public List<Nation> getAllNation(){
        return nationService.list();
    }

    @ApiOperation(value = "获取所有职位")
    @GetMapping("/positions")
    public List<Position> getAllPosition(){
        return positionService.list();
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/deps")
    public List<Department> getAllDeps(){
        return departmentService.getAllDepartment(-1);
    }

    @ApiOperation(value = "获取最大工号")
    @GetMapping("/maxWorkID")
    public RespBean maxWorkID(){
        return employeeEcService.maxWorkID();
    }

    @ApiOperation(value = "添加员工")
    @PostMapping("/addEmp")
    public RespBean addEmployee(@RequestBody Employee employee){
        /**
         * 处理合同期限 保留两位小数
         */
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
        long days = beginContract.until(endContract, ChronoUnit.DAYS);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days/365.00)));

//        if(employeeEcService.addEmployee(employee)){
//            /**
//             * 发送信息到邮件模块
//             */
//            Employee emp = employeeMapper.getEmployee(employee.getId()).get(0);
//            rabbitTemplate.convertAndSend("mail.welcome",emp);
//            return RespBean.success("添加员工成功！",employee);
//        }
        /**
         * 以下代码采用消息投递机制
         */
        if(employeeEcService.addEmployee(employee)){
            Employee emp = employeeMapper.getEmployee(employee.getId()).get(0);
            //数据库记录发送的消息
            String msgId = UUID.randomUUID().toString();
            MailLog mailLog = new MailLog();
            mailLog.setMsgId(msgId);
            mailLog.setCount(0);
            mailLog.setCreateTime(LocalDateTime.now());
            mailLog.setUpdateTime(LocalDateTime.now());
            mailLog.setEid(emp.getId());
            mailLog.setExchange(MailConstants.MAIL_EXCHANGE_NAME);
            mailLog.setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME);
            mailLog.setTryTime(LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT));
            mailLog.setStatus(MailConstants.DELIVERING);

            mailLogMapper.insert(mailLog);
            //发送消息
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_NAME,emp,new CorrelationData(msgId));
            return RespBean.success("添加员工成功！！");
        }
        return RespBean.error("添加员工失败！");
    }

    @ApiOperation(value = "更新员工")
    @PutMapping("/")
    public RespBean updateEmployee(@RequestBody Employee employee){
        if(employeeService.updateById(employee)){
            return RespBean.success("更新员工成功！");
        };
        return RespBean.error("更新员工失败！");
    }

    @ApiOperation(value = "删除员工")
    @PutMapping("/{id}")
    public RespBean deleteEmployee(@RequestParam Integer id){
        if(employeeService.removeById(id)){
            return RespBean.success("删除员工成功！");
        };
        return RespBean.error("删除员工失败！");
    }

    @ApiOperation(value = "导出员工数据")
    @GetMapping(value = "/export",produces = "application/octet-stream")
    public void ExportEmployee(HttpServletResponse response){
        List<Employee> employees = employeeService.getEmployee(null);
        ExportParams exportParams = new ExportParams("员工表","员工表", ExcelType.HSSF );
        Workbook sheets = ExcelExportUtil.exportExcel(exportParams, Employee.class, employees);
        ServletOutputStream outputStream = null;
        try {
            /**
             * 流形式
             * 防止中文乱码
               */
            response.setHeader("content-type","application/octet-stream");
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("员工表.xls","UTF-8"));
            outputStream = response.getOutputStream();
            sheets.write(outputStream) ;
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }finally {
            if(null!=outputStream){
                try {
                    outputStream.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value ="导入员工数据")
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        //去掉标题行
        params.setTitleRows(1);

        List<Nation> nationList = nationService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        List<Department> departmentList = departmentService.list();
        List<Position> positionList = positionService.list();
        List<Joblevel> joblevelList = joblevelService.list();

        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);
            list.forEach(employee->{
                employee.setNationId(nationList.get(nationList.indexOf(new Nation(employee.getNation().getName()))).getId());
                employee.setDepartmentId(departmentList.get(departmentList.indexOf(new Department(employee.getDepartment().getName()))).getId());
                employee.setPoliticId(politicsStatusList.get(politicsStatusList.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
                employee.setPosId(positionList.get(positionList.indexOf(new Position(employee.getPosition().getName()))).getId());
                employee.setJobLevelId(joblevelList.get(joblevelList.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId());
            });
            if(employeeService.saveBatch(list)) {
                return RespBean.success("导入成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("导入失败！");
    }
}
