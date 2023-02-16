/*
 * Copyright (c) 2018, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     : 速通门
 * SdkModule Name : com.unv.fastgate.server.service
 * Date Created: 2019/5/9
 * Creator     : dW5565 dongchenghao
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *------------------------------------------------------------------------------
 */
package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.factory;

import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.manager.DeviceChannelContext;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (C),  2018-2025, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 * <p>
 * FileName : NettyFactory
 * Author   : s04180
 * Date     : 2019/12/30 17:04
 * DESCRIPTION: 通道管理工厂
 * 公网长连接方案，框架直观反应长连接状态是通过地址，IP地址不适用；暂用端口
 * 端口获取序列信息，
 * <p>
 * History:
 * DATE        NAME        DESC
 */
public class ChannelFactory {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelFactory.class);

    protected static final int CHANNEL_MAP_SIZE = 256;

    /**
     * 序列号配置对应终端长连接通道信息
     */
    protected static final Map<String, DeviceChannelContext> CHANNEL_MAP = new ConcurrentHashMap<>(CHANNEL_MAP_SIZE);

    /**
     * 删除通道
     *
     * @param serialNo 设备编码
     */
    public static boolean removeChannelByDeviceCode(String serialNo) throws InterruptedException {
        DeviceChannelContext deviceCtx = CHANNEL_MAP.get(serialNo);
        if (Objects.nonNull(deviceCtx)) {
            deviceCtx.setCtx(null);
            deviceCtx.setLock(false);
            return true;
        }
        LOGGER.error("[Remove_Channel][Device_Code = {}],Connect Channel Doesn't Exist.", serialNo);
        return false;
    }

    /**
     * 新增在线通道
     *
     * @param serialNo
     * @param ctx
     */
    public static void addChannel(String serialNo, ChannelHandlerContext ctx) {
        if (StringUtils.isNotBlank(serialNo) && Objects.nonNull(ctx)) {
            DeviceChannelContext deviceCtx = CHANNEL_MAP.get(serialNo);
            if (Objects.nonNull(deviceCtx)) {
                deviceCtx.setCtx(ctx);
                deviceCtx.setLock(false);
            } else {
                deviceCtx = new DeviceChannelContext(ctx, serialNo, 0);
                CHANNEL_MAP.put(serialNo, deviceCtx);
            }
        } else {
            LOGGER.error("[Add_Channel],Channel Or DeviceCode is Empty.Device:{},Ctx:{}", serialNo, ctx);
        }
    }

    /**
     * 获取  速通门 长连接 通道对象
     *
     * @param serialNo
     * @return
     */
    public static DeviceChannelContext getChannelBySerialNo(String serialNo) {
        if (StringUtils.isBlank(serialNo)) {
            LOGGER.error("[Search_Channel],DeviceCode is Empty");
            return null;
        }
        return CHANNEL_MAP.get(serialNo);
    }

    /**
     * 解锁设备编码为 serialNo 的设备长连接，
     *
     * @param serialNo
     * @throws InterruptedException
     */
    public static void unLockByDeviceCode(String serialNo) throws InterruptedException {
        DeviceChannelContext deviceCtx = CHANNEL_MAP.get(serialNo);
        if (Objects.nonNull(deviceCtx)) {
            deviceCtx.setLock(false);
        } else {
            // CPU
            Thread.sleep(1);
        }
    }

    /**
     * 刷新链接
     *
     * @param serialNo
     * @param ctx
     */
    public static void freshChannel(String serialNo, ChannelHandlerContext ctx) {
        DeviceChannelContext deviceCtx = CHANNEL_MAP.get(serialNo);
        // 通道存在则进行刷新
        if (Objects.nonNull(deviceCtx)) {
            deviceCtx.setCtx(ctx);
        } else {
            // 通道不存在则添加
            addChannel(serialNo, ctx);
        }
    }
}
