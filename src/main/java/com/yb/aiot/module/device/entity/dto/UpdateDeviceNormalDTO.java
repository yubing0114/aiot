package com.yb.aiot.module.device.entity.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 修改设备基础信息dto
 * </p>
 *
 * @author cxy
 * @since 2022-11-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeviceNormalDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备ID")
    private Integer id;

    @ApiModelProperty("产品表id")
    private Integer productInfoId;

    @ApiModelProperty("协议表id")
    private Integer protocolInfoId;

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("是否需要默认布防(false-不需要,true-需要)")
    private Boolean defaultAlarm;

    @ApiModelProperty("设备ip")
    private String ip;

    @ApiModelProperty("设备所在地址")
    private Integer addressId;

    @ApiModelProperty("设备所在房间")
    private Integer roomId;

    @ApiModelProperty("标注状态(false-未标注，true-已标注)")
    private Boolean gisStatus;

    @ApiModelProperty("gis信息json格式")
    private String gisInfo;

    @ApiModelProperty("设备可用年限")
    private double usableYear;
}
