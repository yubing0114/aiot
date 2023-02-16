package com.yb.aiot.module.auth.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 更新角色dto
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
@Data
@TableName("auth_role")
@ApiModel(value = "Role对象", description = "角色表")
public class UpdateRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色码")
    private String code;

    @ApiModelProperty("角色状态(1-正常，2-异常)")
    private Integer status;

    @ApiModelProperty("角色描述")
    private String description;

}
