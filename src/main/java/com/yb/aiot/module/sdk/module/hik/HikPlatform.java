package com.yb.aiot.module.sdk.module.hik;

import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson.JSONObject;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.yb.aiot.module.common.utils.MyFileUtil;
import com.yb.aiot.common.Result;
import com.yb.aiot.utils.IPUtil;
import com.yb.aiot.utils.YmlUtil;
import com.yb.aiot.utils.MyHttpUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 海康平台对接工具类
 * <p>
 *
 * @author: yb
 * @date: 2021/12/14 12:01
 */
public class HikPlatform {

    static JSONObject platformInfo = YmlUtil.getJSONObject("hik");

    /**
     * 开启订阅
     */
    public static void startSubscript() {
        System.out.println("开启订阅");
        // 订阅停车场事件
        subscriptEpidemic();
    }

    /**
     * 订阅停车场事件
     */
    public static void subscriptEpidemic() {
        // 按事件类型订阅事件url
        JSONObject apiInfo = platformInfo.getJSONObject("api");
        String url = apiInfo.getString("subscribe");
        // 封装json对象数据
        JSONObject jsonBody = new JSONObject();
        JSONObject carInOut = apiInfo.getJSONObject("carInOut");
        jsonBody.put("eventDest", IPUtil.getServeUrl() + carInOut.getString("eventDest"));
        // 事件类型
        JSONObject eventTypesInfo = carInOut.getJSONObject("eventTypes");
        Integer[] eventTypes = {eventTypesInfo.getInteger("in"), eventTypesInfo.getInteger("out")};
        jsonBody.put("eventTypes", eventTypes);
        getResourceURL(url, jsonBody);
    }

    /**
     * 调用海康接口方法
     *
     * @param resourcePath
     * @param json
     * @return java.lang.String
     */
    public static String getResourceURL(String resourcePath, JSONObject json) {
        // STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数
        ArtemisConfig.host = platformInfo.getString("ipPort");
        ArtemisConfig.appKey = platformInfo.getString("appkey");
        ArtemisConfig.appSecret = platformInfo.getString("appsecret");
        // STEP2：设置OpenAPI接口的上下文
        final String ARTEMIS_PATH = "/artemis";
        // STEP3：设置接口的URI地址
        final String previewURLsApi = ARTEMIS_PATH + resourcePath;
        Map<String, String> path = new HashMap<>();
        path.put(platformInfo.getString("artemis"), previewURLsApi);
        // STEP4：设置参数提交方式
        String contentType = platformInfo.getString("contentType");
        // STEP6：调用接口,post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, json.toJSONString(), null, null, contentType, null);
        return result;
    }

    /**
     * 解析停车场事件订阅数据
     *
     * @param eventData
     * @return com.yslz.aiot.common.core.domain.Result
     */
    public static Result analyzeEventRcv(JSONObject eventData) {
        JSONObject data = eventData.getJSONObject("params").getJSONArray("events").getJSONObject(0).getJSONObject("data");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("vehicleType", data.getString("carAttributeName"));
        jsonObject.put("licensePlate", data.getString("plateNo"));
        jsonObject.put("inOutType", data.getString("roadwayType"));
        jsonObject.put("roadwayId", data.getString("roadwayIndex"));
        jsonObject.put("time", getTimeMillis(data.getString("time")));
        if (data.getJSONObject("picUrl") != null) {
            jsonObject.put("vehiclePicture", savePicture(data.getJSONObject("picUrl").getString("vehiclePicUrl")));
            String url = YmlUtil.getString("alarm-data", "send-url");
            try {
                MyHttpUtil.post(url, jsonObject.toJSONString());
            } catch (IORuntimeException e) {
                System.out.println("解析停车场事件订阅数据推送接口调用异常!");
            }
            System.out.println(jsonObject + "\n");
        }
        return Result.ok();
    }

    /**
     * 根据时间字符串获取时间戳
     *
     * @param timeStr
     * @return java.lang.String
     */
    public static String getTimeMillis(String timeStr) {
        String[] timeAndHour = timeStr.split("T");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeMillis = null;
        try {
            timeMillis = dateFormat.parse(timeAndHour[0] + " " + timeAndHour[1].split("\\.")[0]).getTime() + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeMillis;
    }

    /**
     * 根据图片url将图片保存到本地指定文件夹
     *
     * @param picUrl
     * @return java.lang.String
     */
    public static String savePicture(String picUrl) {
        URL url;
        String fileName = null;
        try {
            url = new URL(YmlUtil.getString("hik", "picAddr") + picUrl);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            fileName = MyFileUtil.makeFileName("parking");
            FileOutputStream fileOutputStream = new FileOutputStream(MyFileUtil.getAbsolutePath(fileName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MyFileUtil.getDownFileUrl(fileName);
    }

}

