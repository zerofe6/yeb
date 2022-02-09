package com.fz.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 登录实体类
 * @author Bo.Fan
 * @date 2021/9/10 16:21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors
@ApiModel(value="AdminLogin对象",description = "")
public class AdminLoginParam {
    @ApiModelProperty(value = "用户名",required = true)
    private String userName;
    @ApiModelProperty(value = "密码",required = true)
    private String password;
    @ApiModelProperty(value = "验证码",required = true)
    private String code;
}
