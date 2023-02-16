package com.yb.aiot.module.sdk.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 叮咚
 * @date 2022-01-07 14:39:58
 * @email
 */
@Data
public class PtzDto {

    @ApiModelProperty("设备ip")
    private String ip;

    @ApiModelProperty("云台控制命令: 0-上;1-下;2-左;3-右;4-变倍+;5-变倍-")
    private int ptzCommand;

    @ApiModelProperty("云台停止动作或开始动作：0-开始;1-停止")
    private int startOrStop;

}