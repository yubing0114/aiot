package com.yb.aiot.module.sdk.module;

import com.yb.aiot.module.sdk.dto.DeviceDto;

/**
 * <p>
 * sdk服务接口
 * <p>
 *
 * @author author
 * @date 2022/11/17 15:34
 */
public interface SdkModule {

    /**
     * 获取错误码
     *
     * @return java.lang.String
     */
    String getLastError();

    /**
     * 登陆
     *
     * @param device
     * @return void
     */
    Object login(DeviceDto device);

    boolean loginCheck(DeviceDto deviceDto);

    /**
     * 布防
     *
     * @param device
     * @return boolean
     */
    boolean openAlarm(DeviceDto device);

    /**
     * 撤防
     *
     * @param deviceIp
     * @return boolean
     */
    boolean closeAlarm(String deviceIp);

    /**
     * 云台控制
     *
     * @param deviceDto
     * @return boolean
     */
    boolean ptzControl(DeviceDto deviceDto);

    /**
     * 远程控门
     *
     * @param deviceDto
     * @return boolean
     */
    boolean controlDoor(DeviceDto deviceDto);

    /**
     * 道闸控制
     *
     * @param deviceDto
     * @return com.yslz.aiot.module.device.Result
     */
    boolean controlGate(DeviceDto deviceDto);

    String capture(DeviceDto deviceDto);

    /**
     * 设备重启
     *
     * @param deviceDto
     * @return boolean
     */
    boolean reboot(DeviceDto deviceDto);

}
