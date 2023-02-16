package com.yb.aiot.module.device.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 查询设备dto
 * </p>
 *
 * @author cxy
 * @since 2022-11-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryDeviceDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页数")
    private Integer pageIndex;

    @ApiModelProperty("每页个数")
    private Integer pageSize;

    @ApiModelProperty("产品名")
    private String productName;

    @ApiModelProperty("产品分类Id")
    private Integer productTypeId;

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("设备运行状态")
    private Boolean status;

    @ApiModelProperty("设备在线状态")
    private Boolean isOline;

    @ApiModelProperty("布防状态")
    private Boolean alarmStatus;

    @ApiModelProperty("设备ip")
    private String ip;

    @ApiModelProperty("设备所在地址Id")
    private Integer addressId;

    @ApiModelProperty("设备所在房间Id")
    private Integer roomId;
}
