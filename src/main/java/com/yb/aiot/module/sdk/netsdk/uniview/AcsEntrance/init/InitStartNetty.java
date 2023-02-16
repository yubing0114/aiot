package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.init;

import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.manager.RequestDeviceTestThread;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Copyright (C),  2018-2025, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 * <p>
 * FileName : InitStartNetty
 * Author   : s04180
 * Date     : 2020/1/6 16:18
 * DESCRIPTION: 自动执行程序
 * <p>
 * History:
 * DATE        NAME        DESC
 */
@Service
public class InitStartNetty implements ApplicationRunner {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(InitStartNetty.class);

    @Value("${netty.http.connection}")
    private Integer isKeepHttpConnection;

    @Value("${netty.http.port}")
    private Integer isKeepHttpConnectionPort;

    @Autowired
    StartNettyThread startNettyThread;

    @Autowired
    RequestDeviceTestThread requestDeviceTestThread;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        try {
            if (Objects.nonNull(isKeepHttpConnection) && Objects.nonNull(isKeepHttpConnectionPort)) {
                if (0 < isKeepHttpConnection) {
                    ExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                            new BasicThreadFactory.Builder().namingPattern("keep-alive").daemon(true).build());
                    //该服务器，处于挂起状态，不会向下启动，需要线程启动，不影响业务的进行
                    startNettyThread.setIsKeepHttpConnectionPort(isKeepHttpConnectionPort);
                    scheduledExecutorService.execute(startNettyThread);
                }
            }

            // 测试调用终端接口线程
            requestDeviceTestThread.run();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
