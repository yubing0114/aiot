package com.yb.aiot.module.device.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.aiot.module.device.entity.DeviceInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.aiot.module.device.entity.dto.QueryDeviceDTO;
import com.yb.aiot.module.device.entity.vo.DeviceAddressConcatVO;
import com.yb.aiot.module.device.entity.vo.DeviceTableVO;
import com.yb.aiot.module.device.entity.vo.PieCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import com.yb.aiot.module.sdk.dto.DeviceDto;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {

    /**
     * 分页模糊查询
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<vo.entity.device.module.com.yb.aiot.DeviceTableVO>
     */
    IPage<DeviceTableVO> pageListByQuery(Page<DeviceTableVO> page, @Param("query") QueryDeviceDTO query);

    /**
     * 根据ip查询
     *
     * @param ip
     * @return java.util.List<entity.device.module.com.yb.aiot.DeviceInfo>
     */
    DeviceInfo selectByIp(String ip);

    /**
     * 根据设备ip查询设备基础信息
     *
     * @param deviceIp 设备ip
     * @return java.util.List<com.yslz.aiot.module.device.entity.dto.DeviceDto>
     */
    List<DeviceDto> selectBaseInfoByIp(String deviceIp);


    /**
     * 统计产品分类设备数量
     * @return java.util.List<com.yslz.aiot.module.device.entity.vo.PieCountVo>
     */
    List<PieCountVO> countDevicePieByType();

    /**
     * 按设备布防状态统计
     * @return
     */
    Integer countByAlarmStatus(boolean alarmStatus);

    /**
     * 按设备在线状态统计
     * @param isOnline
     * @return
     */
    Integer countByIsOnline(boolean isOnline);

    /**
     * 按设备运行状态统计
     * @return
     */
    Integer countByStatus(boolean status);
    /**
     * 查询DefaultAlarm为true设备基础信息
     *
     * @return java.util.List<dto.sdk.module.com.yb.aiot.DeviceDto>
     */
    List<DeviceDto> selectBaseInfoByDefaultAlarm();

    /**
     * 获取设备完整地址字符串
     * @param id
     * @return com.yslz.aiot.module.entity.vo.DeviceAddressConcatVO
     */
    DeviceAddressConcatVO selectAddressConcatById(Integer id);
}
