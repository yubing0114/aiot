package com.yb.aiot.module.sdk.dto;

import com.yb.aiot.module.sdk.module.SdkModule;
import lombok.Data;

/**
 * @author 叮咚
 * @date 2022-01-07 14:39:58
 */
@Data
public class DeviceDto {

    // Device id
    private Integer deviceId;

    // 产品分类id
    private Integer productTypeId;

    // 产品表id
    private Integer productInfoId;

    // 设备ip
    private String ip;

    // 设备端口号
    private int port;

    // 设备用户名
    private String username;

    // 设备登录密码
    private String password;

    // sdk服务接口
    private SdkModule sdkModule;

    // 视频流地址(摄像头设备使用)
    private String streamUrl;

    // 转码流地址
    private String streamAddr;

    // 物模型json格式
    private String thingsModel;

    // 是否需要默认布防(false-不需要,true-需要)
    private Boolean defaultAlarm;

    // 云台控制命令: 0-上;1-下;2-左;3-右;4-变倍+;5-变倍-
    private int ptzCommand;

    // 云台停止动作或开始动作：0-开始;1-停止
    private int startOrStop;

    // 远程控门 0-关门，1-开门，2-常开，3-常关，4-恢复(恢复为普通状态)
    private int controlCommand;

    // functionId
    private String method;

}