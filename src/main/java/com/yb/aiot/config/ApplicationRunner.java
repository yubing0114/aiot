package com.yb.aiot.config;

import com.yb.aiot.module.auth.service.IUserService;
import com.yb.aiot.module.scheduled.service.ITaskService;
import com.yb.aiot.module.sdk.service.SdkService;
import com.yb.aiot.module.sdk.utils.SdkUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * 程序启动后需要首先执行的业务代码类
 * <p>
 *
 * @author author
 * @date 2022/11/16 10:46
 */
@Component
public class ApplicationRunner implements CommandLineRunner {

    @Resource
    private IUserService userService;

    @Resource
    private IPlaceService placeService;

    @Resource
    private ITaskService taskService;

    @Resource
    private SdkService sdkService;


    @Override
    public void run(String... args) throws Exception {
        userService.addMaster();
        placeService.add();
        SdkUtil.loadSdk();
        // 布防需要默认布防的设备
        sdkService.defaultAlarm();
        // 启动默认启动的定时任务
        taskService.startDefaultTask();
    }

}
