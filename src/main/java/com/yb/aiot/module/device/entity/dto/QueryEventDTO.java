package com.yb.aiot.module.device.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 查询事件dto
 * </p>
 *
 * @author author
 * @since 2022-11-23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryEventDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页数")
    private Integer pageIndex;

    @ApiModelProperty("每页个数")
    private Integer pageSize;

    @ApiModelProperty("事件名")
    private String name;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("事件处理状态")
    private Boolean handleStatus;

    @ApiModelProperty("起始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
