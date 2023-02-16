package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description
 * @Date 2019/5/23 9:48
 * @Created by x06086
 */
public class HttpEncoder<I> extends MessageToMessageEncoder {

    /**
     * request编码器
     */
    private UnvHttpRequestEncoder httpRequestEncoder = new UnvHttpRequestEncoder();

    /**
     * 日志对象
     */
    private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HttpEncoder.class);

    /**
     * response编码器
     */
    private UnvHttpResponseEncoder httpResponseEncoder = new UnvHttpResponseEncoder();


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        if (msg instanceof HttpRequest) {
            httpRequestEncoder.encode(ctx, msg, out);
        } else if (msg instanceof HttpResponse) {
            httpResponseEncoder.encode(ctx, msg, out);
        }
    }
}
