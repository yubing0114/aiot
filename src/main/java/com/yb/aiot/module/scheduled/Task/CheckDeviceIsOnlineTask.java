package com.yb.aiot.module.scheduled.Task;

import com.yb.aiot.module.scheduled.config.ScheduledConfig;
import com.yb.aiot.module.device.service.IDeviceInfoService;

/**
 * <p>
 * 检测设备在线状态定时任务
 * <p>
 *
 * @author author
 * @date 2022/12/6 15:10
 */
public class CheckDeviceIsOnlineTask implements Runnable {

    @Override
    public void run() {
        IDeviceInfoService deviceInfoService = (IDeviceInfoService) ScheduledConfig.getBean("deviceInfoServiceImpl");
        deviceInfoService.checkIsOnline();
    }

}
