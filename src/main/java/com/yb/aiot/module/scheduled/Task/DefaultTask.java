package com.yb.aiot.module.scheduled.Task;

import com.yb.aiot.module.device.service.IDeviceInfoService;
import com.yb.aiot.module.device.service.IEventInfoService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * 不需要动态修改的定时任务类
 * <p>
 *
 * @author author
 * @date 2022/11/24 16:35
 */
@Component
@EnableScheduling
@EnableAsync
public class DefaultTask {

    @Resource
    private IDeviceInfoService deviceInfoService;

    @Resource
    private IEventInfoService eventInfoService;

    @Async
//    @Scheduled(cron = "0 0 1 ? * *")
//    @Scheduled(cron = "0 */1 * * * ?")
//    @Scheduled(cron = "*/5 * * * * ?")
    public void checkIsOnlineTask() {
        System.out.println("Async");
        deviceInfoService.checkIsOnline();
    }

    @Async
    @Scheduled(cron = "0 0 1 ? * *")
    public void deleteEventTask() {
        eventInfoService.deleteByEventSaveDay();
    }

}
