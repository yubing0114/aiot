package com.yb.aiot.module.scheduled.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 添加定时任务dto
 * </p>
 *
 * @author author
 * @since 2022-12-06
 */
@Data
public class AddTaskDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("任务key值（使用bean名称）")
    private String taskKey;

    @ApiModelProperty("cron任务表达式")
    private String cron;

    @ApiModelProperty("任务描述")
    private String depiction;

    @ApiModelProperty("是否需要默认启用(false-否; true-是)")
    private Boolean defaultTask;

}
