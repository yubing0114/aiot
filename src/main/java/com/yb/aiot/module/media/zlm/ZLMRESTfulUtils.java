package com.yb.aiot.module.media.zlm;

import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yb.aiot.module.media.config.MediaConfig;
import com.yb.aiot.common.Result;
import com.yb.aiot.utils.MyHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class ZLMRESTfulUtils {

    @Resource
    MediaConfig mediaConfig;

    private final static Logger logger = LoggerFactory.getLogger(ZLMRESTfulUtils.class);

    public Result proxy(MediaServerItem mediaServerItem, String streamUrl) {
        if (ObjectUtils.isEmpty(mediaServerItem)) {
            mediaServerItem = new MediaServerItem();
            mediaServerItem.setIp(mediaConfig.getIp());
            mediaServerItem.setHttpPort(mediaConfig.getHttpPort());
            mediaServerItem.setSecret(mediaConfig.getSecret());
        }
        String stream = Base64.getEncoder().encodeToString(streamUrl.getBytes(StandardCharsets.UTF_8));
        try {
            JSONObject jsonObject = addStreamProxy(mediaServerItem, "proxy", stream, streamUrl, true, true, "0");
            return playUrl(jsonObject);
        } catch (IORuntimeException e) {
            return Result.fail("流媒体服务异常,请检查!");
        }
    }

    public static Result playUrl(JSONObject result) {
        if (result.getString("code").equals("0") && result.getJSONObject("data") != null) {
            String key = result.getJSONObject("data").getString("key");
            Map<String, String> map = new LinkedHashMap<>();
            map.put("rtsp", "rtsp://" + key);
            map.put("rtmp", "rtmp://" + key);
            String temp = "http://" + key;
            map.put("flv", temp + ".live.flv");
            map.put("mp4", temp + ".live.mp4");
            map.put("hls", temp + "/hls.m3u8");
            map.put("ts", temp + ".live.ts");
            temp = "https://" + key;
            map.put("flv-https", temp + ".live.flv");
            map.put("mp4-https", temp + ".live.mp4");
            map.put("hls-https", temp + "/hls.m3u8");
            map.put("ts-https", temp + ".live.ts");
            temp = "ws://" + key;
            map.put("flv-ws", temp + ".live.flv");
            map.put("mp4-ws", temp + ".live.mp4");
            map.put("ts-ws", temp + ".live.ts");
            return Result.ok(map);
        }
        return Result.fail(result.getString("msg"));
    }


    public JSONObject sendPost(MediaServerItem mediaServerItem, String api, Map<String, Object> param) throws IORuntimeException {
        String url = "http://%s:%s/index/api/%s";
        if (mediaServerItem == null) {
            url = String.format(url, mediaConfig.getIp(), mediaConfig.getHttpPort(), api);
            param.put("secret", mediaConfig.getSecret());
        } else {
            url = String.format(url, mediaServerItem.getIp(), mediaServerItem.getHttpPort(), api);
            param.put("secret", mediaServerItem.getSecret());
        }
        return MyHttpUtil.post(url, JSON.toJSONString(param));

    }

    public void sendGetForImg(MediaServerItem mediaServerItem, String api, HashMap<String, Object> params, String targetPath, String fileName) {
        String url = String.format("http://%s:%s/index/api/%s", mediaServerItem.getIp(), mediaServerItem.getHttpPort(), api);
        logger.debug(url);
        params.put("secret", mediaConfig.getSecret());
//        JSONObject parseUrl = MyHttpUtil.get(url, params);
    }

    public JSONObject getMediaList(MediaServerItem mediaServerItem, String app, String stream, String schema) {
        Map<String, Object> param = new HashMap<>();
        if (app != null) {
            param.put("app", app);
        }
        if (stream != null) {
            param.put("stream", stream);
        }
        if (schema != null) {
            param.put("schema", schema);
        }
        param.put("vhost", "__defaultVhost__");
        return sendPost(mediaServerItem, "getMediaList", param);
    }

    public JSONObject getMediaList(MediaServerItem mediaServerItem, String app, String stream) {
        return getMediaList(mediaServerItem, app, stream);
    }

    public JSONObject getMediaList(MediaServerItem mediaServerItem) {
        return sendPost(mediaServerItem, "getMediaList", null);
    }

    public JSONObject getMediaInfo(MediaServerItem mediaServerItem, String app, String schema, String stream) {
        Map<String, Object> param = new HashMap<>();
        param.put("app", app);
        param.put("schema", schema);
        param.put("stream", stream);
        param.put("vhost", "__defaultVhost__");
        return sendPost(mediaServerItem, "getMediaInfo", param);
    }

    public JSONObject getRtpInfo(MediaServerItem mediaServerItem, String stream_id) {
        Map<String, Object> param = new HashMap<>();
        param.put("stream_id", stream_id);
        return sendPost(mediaServerItem, "getRtpInfo", param);
    }

    public JSONObject addFFmpegSource(MediaServerItem mediaServerItem, String src_url, String dst_url, String timeout_ms, boolean enable_hls, boolean enable_mp4, String ffmpeg_cmd_key) {
        logger.info(src_url);
        logger.info(dst_url);
        Map<String, Object> param = new HashMap<>();
        param.put("src_url", src_url);
        param.put("dst_url", dst_url);
        param.put("timeout_ms", timeout_ms);
        param.put("enable_hls", enable_hls);
        param.put("enable_mp4", enable_mp4);
        param.put("ffmpeg_cmd_key", ffmpeg_cmd_key);
        return sendPost(mediaServerItem, "addFFmpegSource", param);
    }

    public JSONObject delFFmpegSource(MediaServerItem mediaServerItem, String key) {
        Map<String, Object> param = new HashMap<>();
        param.put("key", key);
        return sendPost(mediaServerItem, "delFFmpegSource", param);
    }

    public JSONObject getMediaServerConfig(MediaServerItem mediaServerItem) {
        return sendPost(mediaServerItem, "getServerConfig", null);
    }

    public JSONObject setServerConfig(MediaServerItem mediaServerItem, Map<String, Object> param) {
        return sendPost(mediaServerItem, "setServerConfig", param);
    }

    public JSONObject openRtpServer(MediaServerItem mediaServerItem, Map<String, Object> param) {
        return sendPost(mediaServerItem, "openRtpServer", param);
    }

    public JSONObject closeRtpServer(MediaServerItem mediaServerItem, Map<String, Object> param) {
        return sendPost(mediaServerItem, "closeRtpServer", param);
    }

    public JSONObject listRtpServer(MediaServerItem mediaServerItem) {
        return sendPost(mediaServerItem, "listRtpServer", null);
    }

    public JSONObject startSendRtp(MediaServerItem mediaServerItem, Map<String, Object> param) {
        return sendPost(mediaServerItem, "startSendRtp", param);
    }

    public JSONObject stopSendRtp(MediaServerItem mediaServerItem, Map<String, Object> param) {
        return sendPost(mediaServerItem, "stopSendRtp", param);
    }

    public JSONObject restartServer(MediaServerItem mediaServerItem) {
        return sendPost(mediaServerItem, "restartServer", null);
    }

    public JSONObject addStreamProxy(MediaServerItem mediaServerItem, String app, String stream, String url, boolean enable_hls, boolean enable_mp4, String rtp_type) {
        Map<String, Object> param = new HashMap<>();
        param.put("vhost", mediaServerItem.getIp());
        param.put("app", app);
        param.put("stream", stream);
        param.put("url", url);
        param.put("enable_hls", enable_hls ? 1 : 0);
        param.put("enable_mp4", enable_mp4 ? 1 : 0);
        param.put("enable_rtmp", 1);
        param.put("enable_fmp4", 1);
        param.put("enable_audio", 1);
        param.put("enable_rtsp", 1);
        param.put("add_mute_audio", 1);
        param.put("rtp_type", rtp_type);
        return sendPost(mediaServerItem, "addStreamProxy", param);
    }

    public JSONObject closeStreams(MediaServerItem mediaServerItem, String app, String stream) {
        Map<String, Object> param = new HashMap<>();
        param.put("vhost", "__defaultVhost__");
        param.put("app", app);
        param.put("stream", stream);
        param.put("force", 0);
        return sendPost(mediaServerItem, "close_streams", param);
    }

    public JSONObject getAllSession(MediaServerItem mediaServerItem) {
        return sendPost(mediaServerItem, "getAllSession", null);
    }

    public void kickSessions(MediaServerItem mediaServerItem, String localPortSStr) {
        Map<String, Object> param = new HashMap<>();
        param.put("local_port", localPortSStr);
        sendPost(mediaServerItem, "kick_sessions", param);
    }

    public void getSnap(MediaServerItem mediaServerItem, String flvUrl, int timeout_sec, int expire_sec, String targetPath, String fileName) {
        HashMap<String, Object> param = new HashMap<>(3);
        param.put("url", flvUrl);
        param.put("timeout_sec", timeout_sec);
        param.put("expire_sec", expire_sec);
        sendGetForImg(mediaServerItem, "getSnap", param, targetPath, fileName);
    }

    public JSONObject pauseRtpCheck(MediaServerItem mediaServerItem, String streamId) {
        Map<String, Object> param = new HashMap<>(1);
        param.put("stream_id", streamId);
        return sendPost(mediaServerItem, "pauseRtpCheck", param);
    }

    public JSONObject resumeRtpCheck(MediaServerItem mediaServerItem, String streamId) {
        Map<String, Object> param = new HashMap<>(1);
        param.put("stream_id", streamId);
        return sendPost(mediaServerItem, "resumeRtpCheck", param);
    }

}
