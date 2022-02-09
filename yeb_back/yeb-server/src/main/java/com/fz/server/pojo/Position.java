package com.fz.server.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author fz
 * @since 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false,of = "name")
@NoArgsConstructor
@RequiredArgsConstructor
@TableName("t_position")
@ApiModel(value="Position对象", description="")
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Excel(name = "职位")
    @NonNull
    @ApiModelProperty(value = "职位")
    private String name;


    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asian/Shanghai")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;


}
