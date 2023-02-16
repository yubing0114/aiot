package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (C),  2018-2025, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 * <p>
 * FileName : NettyFactory netty 启动初始化
 * Author   : s04180
 * Date     : 2019/12/30 17:04
 * DESCRIPTION:
 * <p>
 * History:
 * DATE        NAME        DESC
 */

public class LapiNettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LapiNettyServer.class);
    private Integer iKeepHttpConnectionPort;

    public LapiNettyServer(Integer iKeepHttpConnectionPort) {
        this.iKeepHttpConnectionPort = iKeepHttpConnectionPort;
    }

    /**
     * 宏定义
     */
    private final int BOSS_GROUP_THREAD_NUM = 32;
    private final int WORK_GROUP_THREAD_NUM = 64;
    private final int DEAL_BLOCK_SIZE = 256;

    public void createNetty() {
        //bossGroup作为boss,接收传入连接
        EventLoopGroup bossGroup = null;
        EventLoopGroup workerGroup = null;

        bossGroup = new NioEventLoopGroup(BOSS_GROUP_THREAD_NUM);
        workerGroup = new NioEventLoopGroup(WORK_GROUP_THREAD_NUM);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            //服务初始化通道处理
            b.childHandler(new LapiServerInitializer());

            //等待处理的队列大小
            b.option(ChannelOption.SO_BACKLOG, DEAL_BLOCK_SIZE);

            //Boss线程内存池配置.
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //Work线程内存池配置.
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
            //是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
            b.childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口
            ChannelFuture f = b.bind(iKeepHttpConnectionPort).sync();
            //sync()会同步等待连接操作结果，用户线程将在此wait()，直到连接操作完成之后，线程被notify(),用户代码继续执行
            //closeFuture()当Channel关闭时返回一个ChannelFuture,用于链路检测
            LOGGER.info("[NETTY][CREATE][PORT = {}]  ...... START ......", iKeepHttpConnectionPort);
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            //资源优雅释放
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
