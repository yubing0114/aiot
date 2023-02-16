package com.yb.aiot.module.sdk.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 叮咚
 * @date 2022-01-07 14:39:58
 * @email
 */
@Data
public class ControlDto {

    @ApiModelProperty("设备ip")
    private String ip;

    @ApiModelProperty("远程控门：0-关门，1-开门")
    private int controlCommand;

}