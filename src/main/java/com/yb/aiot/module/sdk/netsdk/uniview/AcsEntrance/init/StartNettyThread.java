package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.init;

import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.server.LapiNettyServer;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Copyright (C),  2018-2025, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 * <p>
 * FileName : StartNettyThread
 * Author   : s04180
 * Date     : 2020/1/15 15:38
 * DESCRIPTION:
 * <p>
 * History:
 * DATE        NAME        DESC
 */
@Service
public class StartNettyThread implements Runnable {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StartNettyThread.class);
    /**
     * 宏定义
     */
    private Integer isKeepHttpConnectionPort;

    public void setIsKeepHttpConnectionPort(Integer isKeepHttpConnectionPort) {
        this.isKeepHttpConnectionPort = isKeepHttpConnectionPort;
    }


    @Override
    public void run() {
        try {
            new LapiNettyServer(isKeepHttpConnectionPort).createNetty();
        } catch (Exception e) {
            LOGGER.error("[START_KEEP_HTTP_CONNECTION]", e);
        }
    }

}
