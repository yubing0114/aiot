package com.yb.aiot.module.device.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * <p>
 * 修改设备物模型dto
 * </p>
 *
 * @author cxy
 * @since 2022-11-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeviceThingsModelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备ID")
    private Integer id;

    @ApiModelProperty("物模型json格式")
    private String thingsModel;
}
