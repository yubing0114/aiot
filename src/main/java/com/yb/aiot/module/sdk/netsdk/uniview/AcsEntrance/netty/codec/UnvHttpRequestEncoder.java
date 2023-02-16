package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequestEncoder;

import java.util.List;

/**
 * @Description 放大了netty请求编码器中编码方法的访问权限
 * @Date 2019/5/23 10:08
 * @Created by x06086
 */
public class UnvHttpRequestEncoder extends HttpRequestEncoder {

    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
