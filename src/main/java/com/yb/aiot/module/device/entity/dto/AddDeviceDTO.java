package com.yb.aiot.module.device.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 添加设备dto
 * </p>
 *
 * @author author
 * @since 2022-11-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddDeviceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty("设备可用年限")
    private double usableYear;
}
