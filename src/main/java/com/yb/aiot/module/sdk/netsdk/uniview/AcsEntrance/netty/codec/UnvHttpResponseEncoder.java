package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.List;

/**
 * @Description 自定义的HttpResponseEncoder，就是放大了netty编码器中的访问权限
 * @Date 2019/5/23 10:04
 * @Created by x06086
 */
public class UnvHttpResponseEncoder extends HttpResponseEncoder {

    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
