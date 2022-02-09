package com.fz.server.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_employee")
@ApiModel(value="Employee对象", description="")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "员工姓名")
    @Excel(name = "员工姓名")
    private String name;

    @Excel(name = "性别")
    @ApiModelProperty(value = "性别")
    private String gender;

    @Excel(name = "出生日期",width = 20,format = "yyyy-MM-dd")
    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate birthday;

    @Excel(name = "身份证号",width = 30)
    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @Excel(name = "婚姻状况")
    @ApiModelProperty(value = "婚姻状况")
    private String wedlock;

    @ApiModelProperty(value = "民族")
    private Integer nationId;

    @Excel(name = "籍贯")
    @ApiModelProperty(value = "籍贯")
    private String nativePlace;


    @ApiModelProperty(value = "政治面貌")
    private Integer politicId;

    @Excel(name = "邮箱",width = 30)
    @ApiModelProperty(value = "邮箱")
    private String email;

    @Excel(name = "电话号码",width = 15)
    @ApiModelProperty(value = "电话号码")
    private String phone;

    @Excel(name = "联系地址",width = 40)
    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "所属部门")
    private Integer departmentId;

    @ApiModelProperty(value = "职称ID")
    private Integer jobLevelId;

    @ApiModelProperty(value = "职位ID")
    private Integer posId;

    @Excel(name = "聘用形式")
    @ApiModelProperty(value = "聘用形式")
    private String engageForm;

    @Excel(name = "最高学历")
    @ApiModelProperty(value = "最高学历")
    private String tiptopDegree;

    @Excel(name = "所属专业",width = 20)
    @ApiModelProperty(value = "所属专业")
    private String specialty;

    @Excel(name = "毕业院校",width = 20)
    @ApiModelProperty(value = "毕业院校")
    private String school;

    @Excel(name = "入职日期",width = 20,format = "yyyy-MM-dd" )
    @ApiModelProperty(value = "入职日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate beginDate;

    @Excel(name = "在职状态")
    @ApiModelProperty(value = "在职状态")
    private String workState;

    @Excel(name = "工号")
    @ApiModelProperty(value = "工号")
    private String workID;

    @Excel(name = "合同期限",suffix = "年")
    @ApiModelProperty(value = "合同期限")
    private Double contractTerm;

    @Excel(name = "转正日期" ,width = 20,format = "yyyy-MM-dd")
    @ApiModelProperty(value = "转正日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate conversionTime;

    @Excel(name = "离职日期",width = 20,format = "yyyy-MM-dd")
    @ApiModelProperty(value = "离职日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate notWorkDate;

    @Excel(name = "合同起始日期",width = 20,format = "yyyy-MM-dd")
    @ApiModelProperty(value = "合同起始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate beginContract;

    @Excel(name = "合同终止日期",width = 20,format = "yyyy-MM-dd")
    @ApiModelProperty(value = "合同终止日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate endContract;

    @Excel(name = "工龄",suffix = "年")
    @ApiModelProperty(value = "工龄")
    private Integer workAge;

    @ApiModelProperty(value = "工资账套ID")
    private Integer salaryId;

    @ApiModelProperty(value = "民族")
    @TableField(exist = false)
    @ExcelEntity(name = "民族")
    private Nation nation;

    @ApiModelProperty(value = "政治面貌")
    @TableField(exist = false)
    @ExcelEntity(name = "政治面貌")
    private PoliticsStatus politicsStatus;

    @ApiModelProperty(value = "部门")
    @TableField(exist = false)
    @ExcelEntity(name = "部门")
    private Department department;

    @ApiModelProperty(value = "职称")
    @TableField(exist = false)
    @ExcelEntity(name = "职称")
    private Joblevel joblevel;

    @ApiModelProperty(value = "职位")
    @TableField(exist = false)
    @ExcelEntity(name = "职位")
    private Position position;


    @ApiModelProperty(value = "工资账套")
    @TableField(exist = false)
    private Salary salary;
}
