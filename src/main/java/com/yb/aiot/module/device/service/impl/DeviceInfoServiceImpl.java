package com.yb.aiot.module.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.DeviceInfo;
import com.yb.aiot.module.device.entity.ProductType;
import com.yb.aiot.module.device.entity.dto.AddDeviceDTO;
import com.yb.aiot.module.device.entity.dto.QueryDeviceDTO;
import com.yb.aiot.module.device.entity.dto.UpdateDeviceNormalDTO;
import com.yb.aiot.module.device.entity.dto.UpdateDeviceThingsModelDTO;
import com.yb.aiot.module.device.entity.vo.DeviceTableVO;
import com.yb.aiot.module.device.entity.vo.PieCountVO;
import com.yb.aiot.module.device.mapper.DeviceInfoMapper;
import com.yb.aiot.module.device.mapper.ProductTypeMapper;
import com.yb.aiot.module.device.service.IDeviceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.aiot.utils.IPUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Service
public class DeviceInfoServiceImpl extends ServiceImpl<DeviceInfoMapper, DeviceInfo> implements IDeviceInfoService {

    @Resource
    private ProductTypeMapper productTypeMapper;

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Override
    public Result selectByPage(Integer pageIndex, Integer pageSize, QueryDeviceDTO query) {
        return Result.ok(deviceInfoMapper.pageListByQuery(new Page<DeviceTableVO>(pageIndex, pageSize), query));
    }

    @Override
    public Result selectCameraByPage(Integer pageIndex, Integer pageSize, QueryDeviceDTO query) {
        List<ProductType> ptData = productTypeMapper.selectParentTypeByName("摄像头");
        if (CollectionUtils.isEmpty(ptData)) {
            return Result.fail("还未添加摄像头产品分类");
        }
        query.setProductTypeId(ptData.get(0).getId());
        return Result.ok(deviceInfoMapper.pageListByQuery(new Page<DeviceTableVO>(pageIndex, pageSize), query));
    }

    @Override
    public Result addDevice(AddDeviceDTO addData) {
        if (!ObjectUtils.isEmpty(deviceInfoMapper.selectByIp(addData.getIp()))) {
            return Result.fail("相同IP设备已存在");
        }
        DeviceInfo dData = DeviceInfo.builder().build();
        BeanUtil.copyProperties(addData, dData);
        dData.setIsOnline(false);
        dData.setStatus(false);
        dData.setAlarmStatus(false);
        dData.setCreateTime(LocalDateTime.now());
        dData.setUpdateTime(LocalDateTime.now());
        save(dData);
        return Result.ok("添加成功");
    }

    @Override
    public Result updateDeviceNormal(UpdateDeviceNormalDTO updateData) {
        DeviceInfo dData = getById(updateData.getId());
        if (ObjectUtils.isEmpty(dData)) {
            return Result.fail("设备不存在");
        }
        DeviceInfo exitData = deviceInfoMapper.selectByIp(updateData.getIp());
        if (!ObjectUtils.isEmpty(exitData) && !exitData.getId().equals(updateData.getId())) {
            return Result.fail("相同IP设备已存在");
        }
        dData.setName(updateData.getName());
        dData.setProductInfoId(updateData.getProductInfoId());
        dData.setProtocolInfoId(updateData.getProtocolInfoId());
        dData.setAddressId(updateData.getAddressId());
        dData.setRoomId(updateData.getRoomId());
        dData.setIp(updateData.getIp());
        dData.setDefaultAlarm(updateData.getDefaultAlarm());
        dData.setGisStatus(updateData.getGisStatus());
        dData.setGisInfo(updateData.getGisInfo());
        dData.setUpdateTime(LocalDateTime.now());
        dData.setUsableYear(updateData.getUsableYear());
        updateById(dData);
        return Result.ok("修改成功");
    }

    @Override
    public Result updateDeviceThingsModel(UpdateDeviceThingsModelDTO updateData) {
        DeviceInfo dData = getById(updateData.getId());
        if (ObjectUtils.isEmpty(dData)) {
            return Result.fail("设备不存在");
        }
        dData.setThingsModel(updateData.getThingsModel());
        dData.setUpdateTime(LocalDateTime.now());
        updateById(dData);
        return Result.ok("修改成功");
    }

    @Override
    public Result selectById(Integer deviceId) {
        DeviceInfo dData = getById(deviceId);
        if (ObjectUtils.isEmpty(dData)) {
            Result.fail("设备不存在");
        }
        return Result.ok(dData);
    }

    @Override
    public Result deleteById(Integer deviceId) {
        if (!removeById(deviceId)) {
            Result.fail("设备不存在");
        }
        return Result.ok("删除成功");
    }

    @Override
    public Result countDevicePieByType() {
        return Result.ok(deviceInfoMapper.countDevicePieByType());
    }

    @Override
    public Result selectAddressConcatById(Integer deviceId){
        return Result.ok(deviceInfoMapper.selectAddressConcatById(deviceId));
    }

    @Override
    public Result countDeviceByStatus() {
        List<PieCountVO> pvoList = new ArrayList<>();
        PieCountVO normalDevice = new PieCountVO();
        normalDevice.setName("正常");
        normalDevice.setValue(deviceInfoMapper.countByStatus(true));
        pvoList.add(normalDevice);
        PieCountVO errorDevice = new PieCountVO();
        errorDevice.setName("异常");
        errorDevice.setValue(deviceInfoMapper.countByStatus(false));
        pvoList.add(errorDevice);
        PieCountVO onlineDevice = new PieCountVO();
        onlineDevice.setName("在线");
        onlineDevice.setValue(deviceInfoMapper.countByIsOnline(true));
        pvoList.add(onlineDevice);
        PieCountVO offlineDevice = new PieCountVO();
        offlineDevice.setName("离线");
        offlineDevice.setValue(deviceInfoMapper.countByIsOnline(false));
        pvoList.add(offlineDevice);
        return Result.ok(pvoList);
    }

    @Override
    public Result countDeviceByAlarmStatus() {
        List<PieCountVO> pvoList = new ArrayList<>();
        PieCountVO normalDevice = new PieCountVO();
        normalDevice.setName("已部防");
        normalDevice.setValue(deviceInfoMapper.countByAlarmStatus(true));
        pvoList.add(normalDevice);
        PieCountVO errorDevice = new PieCountVO();
        errorDevice.setName("未布防");
        errorDevice.setValue(deviceInfoMapper.countByAlarmStatus(false));
        pvoList.add(errorDevice);
        return Result.ok(pvoList);
    }

    @Override
    public void checkIsOnline() {
        List<DeviceInfo> deviceInfoList = list();
        if (CollectionUtils.isEmpty(deviceInfoList)) {
            return;
        }
        for (DeviceInfo deviceInfo : deviceInfoList) {
            boolean isOnline = IPUtil.ping(deviceInfo.getIp());
            if (ObjectUtils.isEmpty(deviceInfo.getIsOnline()) || (deviceInfo.getIsOnline() != isOnline)) {
                deviceInfo.setIsOnline(isOnline);
                updateById(deviceInfo);
            }
        }
    }

    @Override
    public Result checkIsOnline(String deviceIp) {
        DeviceInfo deviceInfo = deviceInfoMapper.selectByIp(deviceIp);
        if (ObjectUtils.isEmpty(deviceInfo)) {
            return Result.fail(String.format("ip为%s的设备不存在", deviceIp));
        }
        boolean isOnline = IPUtil.ping(deviceInfo.getIp());
        if (ObjectUtils.isEmpty(deviceInfo.getIsOnline()) || (deviceInfo.getIsOnline() != isOnline)) {
            deviceInfo.setIsOnline(isOnline);
            updateById(deviceInfo);
        }
        return Result.ok(isOnline);
    }

}
