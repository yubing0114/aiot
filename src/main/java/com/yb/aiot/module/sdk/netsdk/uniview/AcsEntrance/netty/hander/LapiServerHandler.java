package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.hander;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.constant.LapiUriConstat;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.manager.ResponseDeviceManager;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.bean.AccessBackDTO;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.bean.HeartBackDTO;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.bean.HeartRequestDTO;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.manager.DeviceChannelContext;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.factory.ChannelFactory;
import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.netty.factory.HttpResponseFactory;
import com.yb.aiot.module.sdk.utils.CallbackUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

/**
 * Copyright (C),  2018-2025, ZheJiang Uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 * <p>
 * FileName : HttpHeartHandler
 *
 * @Author : s04180
 * Date     : 2019/12/30 17:04
 * DESCRIPTION:
 * <p>
 * History:
 * DATE        NAME        DESC
 */
public class LapiServerHandler extends SimpleChannelInboundHandler {

    /**
     * 日志信息
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LapiServerHandler.class);

    /**
     * 设备序列号
     */
    public volatile String serialNo;

    /**
     * base64字符串转化成图片
     *
     * @param: imgStr
     * @return: boolean
     */
    public static boolean GenerateImage(String imgStr) {
        // 图像数据为空
        if (imgStr == null) {
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            String imgFilePath = System.getProperty("user.dir") + "\\img\\" + System.currentTimeMillis() + ".jpg";
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 读取通道,过滤并处理心跳消息
     *
     * @param ctx 通道上下文
     * @param msg 消息
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 区分是response 还是 request
        if (!(msg instanceof FullHttpRequest)) {
            FullHttpRequest httpRequest = (FullHttpRequest) msg;
            // 消息体内容
            String content = httpRequest.content().toString(CharsetUtil.UTF_8);
            // 心跳保活消息
            if (httpRequest.uri().contains(LapiUriConstat.HEART_URI)) {
                // 回包并存下设备的连接通道ctx
                saveDeviceCtx(content, ctx);
                heartResponse(ctx);
                // 设备上报的出入记录信息
            } else if (httpRequest.uri().contains(LapiUriConstat.ACCESS_RECEIVE_URI)) {
                String accessRecord = httpRequest.content().toString(CharsetUtil.UTF_8);
                JSONObject jsonObject = JSON.parseObject(accessRecord);
                accessResponse(ctx, jsonObject.getLong("Seq"), 0);
                // 宇视门禁测温数据推送
                CallbackUtil.sendUniviewData(jsonObject);
            } else {
                // 其他请求信息暂未处理,直接透传到后面可自定义的 handle
                ctx.fireChannelRead(msg);
            }
        } else if (msg instanceof FullHttpResponse) {
            // 平台请求,设备的回复消息
            FullHttpResponse response = (FullHttpResponse) msg;
            HttpResponseFactory.responseHandle(ctx, response);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 心跳回包拼装
     *
     * @return
     * @throws Exception
     */
    private void heartResponse(ChannelHandlerContext ctx) {
        HeartBackDTO heartBackDO = new HeartBackDTO(LapiUriConstat.HEART_URI, 0, new Date());
        ResponseDeviceManager.responseDevice(ctx, heartBackDO);
        LOGGER.info("[SERIAL_NO][{}] 心跳回包正常！客户端IP: {}", serialNo, ctx.channel().remoteAddress());
    }

    /**
     * 响应终端
     * StatusCode：0：平台接收成功，1：平台接收失败
     * StatusString：Succeed/Common Fail
     * RecordID：通行记录ID（对应过人记录里的"Seq"字段）
     *
     * @param ctx
     * @param seq        记录id
     * @param statusCode 0-成功 1-失败
     */
    public void accessResponse(ChannelHandlerContext ctx, Long seq, Integer statusCode) {
        AccessBackDTO accessRecordBack = new AccessBackDTO(LapiUriConstat.ACCESS_RESPONSE_URI, statusCode, seq);
        ResponseDeviceManager.responseDevice(ctx, accessRecordBack);
    }

    /**
     * 保存链接
     *
     * @param strRequest
     * @param ctx
     * @throws Exception
     */
    private void saveDeviceCtx(String strRequest, ChannelHandlerContext ctx) {
        HeartRequestDTO request = JSONObject.parseObject(strRequest, HeartRequestDTO.class);
        String serialNo = request.getStrDeviceCode();
        this.serialNo = serialNo;
        DeviceChannelContext deviceCtx = ChannelFactory.getChannelBySerialNo(serialNo);
        if (Objects.isNull(deviceCtx)) {
            // 添加设备的链接通道
            ChannelFactory.addChannel(serialNo, ctx);
        } else {
            // 刷新设备对应的链接通道
            ChannelFactory.freshChannel(serialNo, ctx);
        }
    }

    /**
     * 添加channel时
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        Channel incoming = ctx.channel();
        LOGGER.debug("SimpleChatClient:" + incoming.remoteAddress() + "通道被添加");
    }

    /**
     * 删除channel时
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        LOGGER.debug("SimpleChatClient:{},通道被删除", channel.remoteAddress());
    }

    /**
     * 服务端监听到客户端不活动
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 服务端接收到客户端掉线通知
        Channel incoming = ctx.channel();
        LOGGER.error("SimpleChatClient: {}  掉线", incoming.remoteAddress());
    }

    /**
     * 服务端监听到客户端活动
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 服务端接收到客户端上线通知
        Channel incoming = ctx.channel();
        LOGGER.debug("SimpleChatClient:" + incoming.remoteAddress() + "上线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel channel = ctx.channel();
        LOGGER.error("SimpleChatClient:{},异常断开,{}", channel.remoteAddress(), cause);
        // 出现异常判断下通道是否还可用
        if (!channel.isWritable()) {
            ctx.close();
            HttpResponseFactory.removeResponse(ctx);
        }
    }
}
