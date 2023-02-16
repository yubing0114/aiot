package com.yb.aiot.module.device.service;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.DeviceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.aiot.module.device.entity.dto.AddDeviceDTO;
import com.yb.aiot.module.device.entity.dto.QueryDeviceDTO;
import com.yb.aiot.module.device.entity.dto.UpdateDeviceNormalDTO;
import com.yb.aiot.module.device.entity.dto.UpdateDeviceThingsModelDTO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface IDeviceInfoService extends IService<DeviceInfo> {


    /**
     * 分页请求设备数据
     *
     * @param pageIndex
     * @param pageSize
     * @param query
     * @return common.com.yb.aiot.Result
     */
    Result selectByPage(Integer pageIndex, Integer pageSize, QueryDeviceDTO query);

    /**
     * 分页请求摄像头设备数据
     * @param pageIndex
     * @param pageSize
     * @param query
     * @return common.com.yb.aiot.Result
     */
    Result selectCameraByPage(Integer pageIndex, Integer pageSize, QueryDeviceDTO query);

    /**
     * 添加设备
     *
     * @param addData
     * @return common.com.yb.aiot.Result
     */
    Result addDevice(AddDeviceDTO addData);

    /**
     * 修改设备基础信息
     *
     * @param updateData
     * @return common.com.yb.aiot.Result
     */
    Result updateDeviceNormal(UpdateDeviceNormalDTO updateData);

    /**
     * 修改设备物模型信息
     *
     * @param updateData
     * @return common.com.yb.aiot.Result
     */
    Result updateDeviceThingsModel(UpdateDeviceThingsModelDTO updateData);

    /**
     * 根据Id获取设备
     *
     * @param deviceId
     * @return common.com.yb.aiot.Result
     */
    Result selectById(Integer deviceId);

    /**
     * 根据Id删除设备
     *
     * @param deviceId
     * @return common.com.yb.aiot.Result
     */
    Result deleteById(Integer deviceId);

    /**
     * 获取总分类设备数量
     *
     * @return common.com.yb.aiot.Result
     */
    Result countDevicePieByType();

    /**
     * 获取设备完整地址字符串
     * @param deviceId
     * @return common.com.yb.aiot.Result
     */
    Result selectAddressConcatById(Integer deviceId);

    /**
     * 获取设备布防状态统计数据数据
     *
     * @return common.com.yb.aiot.Result
     */
    Result countDeviceByAlarmStatus();

    /**
     * 获取设备运行状态统计数据
     *
     * @return common.com.yb.aiot.Result
     */
    Result countDeviceByStatus();

    /**
     * 定时检测并更新设备在线状态
     */
    void checkIsOnline();

    /**
     * 检测并更新设备在线状态
     *
     * @param deviceIp 设备ip
     * @return common.com.yb.aiot.Result
     */
    Result checkIsOnline(String deviceIp);

}
