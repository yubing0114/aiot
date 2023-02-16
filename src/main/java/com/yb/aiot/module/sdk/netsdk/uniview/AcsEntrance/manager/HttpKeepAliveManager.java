package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.manager;

import com.alibaba.fastjson.JSON;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.factory.ChannelFactory;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.factory.HttpResponseFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class HttpKeepAliveManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpKeepAliveManager.class);

    private static final int KEEP_ALIVE_CONNECTION_TIME_OUT = 10000;

    /**
     * 通过长连接获取返回结果
     *
     * @param serialNo    序列号
     * @param httpRequest 请求类
     * @return
     * @throws InterruptedException
     */
    public synchronized static Map<String, String> getResponseMapByKeepAliveConnection(String serialNo, FullHttpRequest httpRequest) {
        Map responseMap = null;
        Integer responseKey = null;
        try {
            //通过设备编码获取，通道
            ChannelHandlerContext ctx = null;
            DeviceChannelContext deviceChannelContext = ChannelFactory.getChannelBySerialNo(serialNo);
            if (Objects.nonNull(deviceChannelContext)) {
                ctx = deviceChannelContext.getCtx();
            }
            if (Objects.isNull(ctx)) {
//                LOGGER.error("未找到设备:{}的连接信息", serialNo);
                return null;
            } else {
                ChannelFactory.unLockByDeviceCode(serialNo);
            }
            //发起请求前，先做移除
            responseKey = ctx.hashCode();
            HttpResponseFactory.RESPONSE_DATA.remove(responseKey);
            String response = "";
            if (ctx != null && ctx.channel().isWritable()) {
                ctx.writeAndFlush(httpRequest);
                // 处理成 responseMap
                response = HttpResponseFactory.getResponse(ctx, KEEP_ALIVE_CONNECTION_TIME_OUT);
            } else {
                LOGGER.error("[DEVICE_CODE = {}]...... netty queue is line up, return fail ......", serialNo);
                ctx.channel().close();
                deviceChannelContext.setCtx(null);
            }
            if (StringUtils.isNotBlank(response)) {
                response.replace("\"Data\": \n" + "\t}", "\"Data\":{}");
                responseMap = JSON.parseObject(response, Map.class);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            //异常移除链接
            try {
                ChannelFactory.removeChannelByDeviceCode(serialNo);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            try {
                ChannelFactory.unLockByDeviceCode(serialNo);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } finally {
            if (Objects.nonNull(responseKey)) {
                //移除数据 无论是否取到，做移除
                HttpResponseFactory.RESPONSE_DATA.remove(responseKey);
                try {
                    ChannelFactory.unLockByDeviceCode(serialNo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseMap;
    }
}
