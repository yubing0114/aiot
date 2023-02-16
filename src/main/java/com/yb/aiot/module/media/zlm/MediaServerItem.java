package com.yb.aiot.module.media.zlm;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;

@Schema(description = "流媒体服务信息")
@Data
public class MediaServerItem {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "IP")
    private String ip;

    @Schema(description = "hook使用的IP（zlm访问WVP使用的IP）")
    private String hookIp;

    @Schema(description = "SDP IP")
    private String sdpIp;

    @Schema(description = "流IP")
    private String streamIp;

    @Schema(description = "HTTP端口")
    private int httpPort;

    @Schema(description = "HTTPS端口")
    private int httpSSlPort;

    @Schema(description = "RTMP端口")
    private int rtmpPort;

    @Schema(description = "RTMPS端口")
    private int rtmpSSlPort;

    @Schema(description = "RTP收流端口（单端口模式有用）")
    private int rtpProxyPort;

    @Schema(description = "RTSP端口")
    private int rtspPort;

    @Schema(description = "RTSPS端口")
    private int rtspSSLPort;

    @Schema(description = "是否开启自动配置ZLM")
    private boolean autoConfig;

    @Schema(description = "ZLM鉴权参数")
    private String secret;

    @Schema(description = "某个流无人观看时，触发hook.on_stream_none_reader事件的最大等待时间，单位毫秒")
    private int streamNoneReaderDelayMS;

    @Schema(description = "keepalive hook触发间隔,单位秒")
    private int hookAliveInterval;

    @Schema(description = "是否使用多端口模式")
    private boolean rtpEnable;

    @Schema(description = "状态")
    private boolean status;

    @Schema(description = "多端口RTP收流端口范围")
    private String rtpPortRange;

    @Schema(description = "RTP发流端口范围")
    private String sendRtpPortRange;

    @Schema(description = "assist服务端口")
    private int recordAssistPort;

    @Schema(description = "创建时间")
    private String createTime;

    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "上次心跳时间")
    private String lastKeepaliveTime;

    @Schema(description = "是否是默认ZLM")
    private boolean defaultServer;

    @Schema(description = "SSRC信息")
    private SsrcConfig ssrcConfig;

    @Schema(description = "当前使用到的端口")
    private int currentPort;


    /**
     * 每一台ZLM都有一套独立的SSRC列表
     * 在ApplicationCheckRunner里对mediaServerSsrcMap进行初始化
     */
    @Schema(description = "ID")
    private HashMap<String, SsrcConfig> mediaServerSsrcMap;

    public MediaServerItem() {
    }

}
