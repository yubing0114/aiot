package com.yb.aiot.module.scheduled.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 定时任务配置表
 * </p>
 *
 * @author author
 * @since 2022-12-06
 */
@Data
@ApiModel(value = "Task对象", description = "定时任务配置表")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("任务key值（使用bean名称）")
    private String taskKey;

    @ApiModelProperty("cron任务表达式")
    private String cron;

    @ApiModelProperty("任务描述")
    private String depiction;

    @ApiModelProperty("状态(false-未启用; true-启用)")
    private Boolean status;

    @ApiModelProperty("是否需要默认启用(false-否; true-是)")
    private Boolean defaultTask;

    @ApiModelProperty("启用时间")
    private LocalDateTime startTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
