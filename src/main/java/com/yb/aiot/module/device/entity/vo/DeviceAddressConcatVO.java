package com.yb.aiot.module.device.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 设备安装地址VO
 * </p>
 *
 * @author cxy
 * @since 2022-12-19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceAddressConcatVO implements Serializable{

    @ApiModelProperty("地址名称")
    public String addressName;

    @ApiModelProperty("房间名称")
    public String roomName;
}
