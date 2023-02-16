package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.server;

import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.codec.HttpDecoder;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.codec.HttpEncoder;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.hander.LapiServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * Copyright (C),  2018-2025, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 * <p>
 * FileName : LapiServerInitializer
 * Author   : s04180
 * Date     : 2019/12/30 17:35
 * DESCRIPTION:  netty  消息处理核心层
 * <p>
 * History:
 * DATE        NAME        DESC
 */
public class LapiServerInitializer extends ChannelInitializer<SocketChannel> {

    private final int READ_TIME_OUT = 60;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        //通道空闲超时时间
        pipeline.addLast(new IdleStateHandler(READ_TIME_OUT, READ_TIME_OUT, READ_TIME_OUT, TimeUnit.SECONDS));
        pipeline.addLast(new HttpDecoder());
        pipeline.addLast(new HttpEncoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(10 * 1024 * 1024));
        //心跳
        pipeline.addLast(new LapiServerHandler());
    }
}
