package com.yb.aiot.module.sdk.service;

import com.yb.aiot.module.device.entity.DeviceInfo;
import com.yb.aiot.module.device.mapper.DeviceInfoMapper;
import com.yb.aiot.module.device.service.IDeviceInfoService;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.sdk.dto.ControlDto;
import com.yb.aiot.module.sdk.dto.DeviceDto;
import com.yb.aiot.module.sdk.dto.PtzDto;
import com.yb.aiot.module.sdk.module.SdkModule;
import com.yb.aiot.module.sdk.utils.SdkUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * SDK服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Service
public class SdkService {

    @Resource
    private DeviceInfoMapper deviceInfoMapper;

    @Resource
    private IDeviceInfoService deviceInfoService;

    @Resource
    private DeviceService deviceService;

    public Result loadSdk(String manufacturer) {
        if (SdkUtil.loadSdk(manufacturer)) {
            return Result.ok(true);
        }
        return Result.fail(false);
    }

    public void defaultAlarm() {
        // 将所有设备布防状态设为未布防状态
        List<DeviceInfo> deviceInfoList = deviceInfoService.list();
        deviceInfoList.forEach(deviceInfo -> {
            deviceInfo.setAlarmStatus(false);
        });
        deviceInfoService.updateBatchById(deviceInfoList);
        // 布防默认需要布防的设备
        List<DeviceDto> deviceDtoList = deviceInfoMapper.selectBaseInfoByDefaultAlarm();
        for (DeviceDto deviceDto : deviceDtoList) {
            String manufacturer = SdkUtil.getManufacturer(deviceDto.getThingsModel());
            if (ObjectUtils.isEmpty(manufacturer)) {
                continue;
            }
            SdkModule module = SdkUtil.getModule(manufacturer);
            if (module.openAlarm(deviceDto)) {
                deviceService.updateAlarmStatus(deviceDto.getDeviceId(), true);
            }
        }
    }

    public Result openAlarm(String deviceIp) {
        DeviceDto deviceDto = deviceService.getDeviceDto(deviceIp);
        if (ObjectUtils.isEmpty(deviceDto)) {
            return Result.fail(String.format("ip为%s的设备不存在", deviceIp));
        }
        if (deviceDto.getSdkModule().openAlarm(deviceDto) && deviceService.updateAlarmStatus(deviceDto.getDeviceId(), true)) {
            return Result.ok("布防成功");
        }
        return Result.fail("布防失败");
    }

    public Result login(String deviceIp) {
        DeviceDto deviceDto = deviceService.getDeviceDto(deviceIp);
        if (ObjectUtils.isEmpty(deviceDto)) {
            return Result.fail(String.format("ip为%s的设备不存在", deviceIp));
        }
        SdkModule module = deviceDto.getSdkModule();
        if (module.loginCheck(deviceDto)) {
            return Result.ok("设备登陆成功");
        }
        return Result.fail("设备登陆失败" + module.getLastError());
    }

    public Result closeAlarm(String deviceIp) {
        DeviceDto deviceDto = deviceService.getDeviceDto(deviceIp);
        if (ObjectUtils.isEmpty(deviceDto)) {
            return Result.fail(String.format("ip为%s的设备不存在", deviceIp));
        }
        if (deviceDto.getSdkModule().closeAlarm(deviceIp) && deviceService.updateAlarmStatus(deviceDto.getDeviceId(), false)) {
            return Result.ok("撤防成功");
        }
        return Result.fail("撤防失败");
    }


    public Result ptzControl(PtzDto ptzDto) {
        DeviceDto deviceDto = deviceService.getDeviceDto(ptzDto.getIp());
        if (ObjectUtils.isEmpty(deviceDto)) {
            return Result.fail(String.format("ip为%s的设备不存在", ptzDto.getIp()));
        }
        deviceDto.setControlCommand(ptzDto.getPtzCommand());
        deviceDto.setStartOrStop(ptzDto.getStartOrStop());
        if (deviceDto.getSdkModule().ptzControl(deviceDto)) {
            return Result.ok("控制成功");
        }
        return Result.fail("控制失败");
    }

    public Result controlDoor(ControlDto doorDto) {
        DeviceDto deviceDto = deviceService.getDeviceDto(doorDto.getIp());
        if (ObjectUtils.isEmpty(deviceDto)) {
            return Result.fail(String.format("ip为%s的设备不存在", doorDto.getIp()));
        }
        deviceDto.setControlCommand(doorDto.getControlCommand());
        if (deviceDto.getSdkModule().controlDoor(deviceDto)) {
            return Result.ok("控制成功");
        }
        return Result.fail("控制失败");
    }

    public Result controlGate(ControlDto controlDto) {
        DeviceDto deviceDto = deviceService.getDeviceDto(controlDto.getIp());
        if (ObjectUtils.isEmpty(deviceDto)) {
            return Result.fail(String.format("ip为%s的设备不存在", controlDto.getIp()));
        }
        deviceDto.setControlCommand(controlDto.getControlCommand());
        if (deviceDto.getSdkModule().controlGate(deviceDto)) {
            return Result.ok("控制成功");
        }
        return Result.fail("控制失败");
    }

    public Result capture(String deviceIp) {
        DeviceDto deviceDto = deviceService.getDeviceDto(deviceIp);
        if (ObjectUtils.isEmpty(deviceDto)) {
            return Result.fail(String.format("ip为%s的设备不存在", deviceIp));
        }
        if (deviceDto.getSdkModule().capture(deviceDto) != null) {
            return Result.ok("抓图成功");
        }
        return Result.fail("抓图失败");
    }

    public Result reboot(String deviceIp) {
        DeviceDto deviceDto = deviceService.getDeviceDto(deviceIp);
        if (ObjectUtils.isEmpty(deviceDto)) {
            return Result.fail(String.format("ip为%s的设备不存在", deviceIp));
        }
        if (deviceDto.getSdkModule().reboot(deviceDto)) {
            return Result.ok("重启成功");
        }
        return Result.fail("重启失败");
    }

    public Result findCard(String deviceIp) {
        return null;
    }

}
