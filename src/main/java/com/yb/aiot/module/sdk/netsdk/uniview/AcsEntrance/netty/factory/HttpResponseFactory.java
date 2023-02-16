package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.factory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 处理客户端的响应
 * @Date 2019/5/22 10:21
 * @Created by x06086
 */
@Service
public class HttpResponseFactory {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResponseFactory.class);
    /**
     * 返回结果集合 <ctx.hasCode, <httpUrl, httpResponse>>
     */
    public static final ConcurrentHashMap<Integer, String> RESPONSE_DATA = new ConcurrentHashMap<>(128);
    public static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, String>> KEEP_ALIVE_RESPONSE_MAP = new ConcurrentHashMap<>(256);

    /**
     * 处理响应数据
     *
     * @param ctx          - 通道信息
     * @param httpResponse - 响应结果
     */
    public static void responseHandle(ChannelHandlerContext ctx, FullHttpResponse httpResponse) {
        try {
            String responseBody = httpResponse.content().toString(CharsetUtil.UTF_8);
            int iHashCodeKey = ctx.channel().hashCode();
            //收到消息的响应的长连接MAP 无 此 hashCode 则向下
            ConcurrentHashMap<Integer, String> temMap = KEEP_ALIVE_RESPONSE_MAP.get(iHashCodeKey);
            if (null != temMap) {
                temMap.put(iHashCodeKey, responseBody);
            } else {
                //单设备单接口做成并行,设备这里使用通道哈希值代替
                if (null != RESPONSE_DATA.putIfAbsent(iHashCodeKey, responseBody)) {
                    //出现多条响应入队的情况,记录错误日志
                    LOGGER.error("[KEEP_ALIVE][RESPONSE]-The {}- RESPONSE queue is full,this response will be drop. {}", iHashCodeKey, responseBody);
                }
                //如果已经有响应未提取,以后面的响应为准
                RESPONSE_DATA.put(iHashCodeKey, responseBody);
            }
        } catch (Exception e) {
            LOGGER.error("Handle response failed for {}", e.getMessage(), e);
        }
    }

    /**
     * 从响应map中清除该设备(通道)的所有响应
     *
     * @param ctx
     */
    public static void removeResponse(ChannelHandlerContext ctx) {
        RESPONSE_DATA.remove(ctx.channel().hashCode());
    }


    /**
     * 获取响应带超时
     *
     * @param ctx     通道
     * @param timeout 超时时间/ms
     * @return null:无响应/response:响应
     */
    public static String getResponse(ChannelHandlerContext ctx, long timeout) throws InterruptedException {
        if (null == ctx) {
            throw new IllegalArgumentException();
        }
        long start = System.currentTimeMillis();
        //如果没响应一直阻塞,直到超时时间到
        while ((null == RESPONSE_DATA.get(ctx.channel().hashCode())) && (System.currentTimeMillis() - start) < timeout) {
            Thread.sleep(1);
        }
        return getResponse(ctx);
    }

    /**
     * 获取响应
     *
     * @param ctx 通道
     * @return null:无响应/response:响应
     */
    public static String getResponse(ChannelHandlerContext ctx) {
        if (null == ctx) {
            throw new IllegalArgumentException();
        }
        try {
            return RESPONSE_DATA.get(ctx.channel().hashCode());
        } finally {
            //取完响应就要清空
            removeResponse(ctx);
        }
    }

}
