/**
 * Copyright (C),  2011-2020, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 * <p>
 * FileName : DeviceResponseManager
 * Author   : z06274
 * Date     : 2020/7/17 9:47
 * DESCRIPTION: 回复设备消息业务类
 * <p>
 * History:
 * DATE        NAME        DESC
 */
package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.manager;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderValues;

import java.util.Objects;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author z06274
 * @date 2020/7/17 9:47
 * @description 回复设备消息业务类
 */
public class ResponseDeviceManager {

    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONNECTION = "Connection";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    /**
     * 设置响应消息
     *
     * @param object
     * @return
     */
    public static FullHttpResponse setResponseInfo(Object object) {
        FullHttpResponse response = setResponseBody(object);
        return setResponseHeaders(response);
    }

    /**
     * 设置响应消息体
     *
     * @param object
     */
    public static FullHttpResponse setResponseBody(Object object) {
        String body = "";
        FullHttpResponse response = null;
        if (Objects.nonNull(object)) {
            body = JSON.toJSONString(object);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body.getBytes()));
        } else {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        }
        return response;
    }

    /**
     * @param ctx    连接通道
     * @param object 响应信息
     */
    public static void responseDevice(ChannelHandlerContext ctx, Object object) {
        FullHttpResponse response = setResponseInfo(object);
        ctx.writeAndFlush(response);
    }

    /**
     * 设置回复消息体header信息
     * CONTENT_TYPE 如果不一致，可自定义传参
     *
     * @param response
     * @return
     */
    public static FullHttpResponse setResponseHeaders(FullHttpResponse response) {
//		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
//		response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
        response.headers().set(CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
//		response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        response.headers().set(CONNECTION, HttpHeaderValues.CLOSE);
//		response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaderValues.CLOSE);
        response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, HttpHeaderValues.CLOSE);
        return response;
    }
}
