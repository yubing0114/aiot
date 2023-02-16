package com.yb.aiot.module.sdk.utils;

import cn.hutool.core.io.IORuntimeException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yb.aiot.module.common.utils.MyFileUtil;
import com.yb.aiot.module.device.entity.EventInfo;
import com.yb.aiot.module.device.entity.EventType;
import com.yb.aiot.module.sdk.dto.DeviceDto;
import com.yb.aiot.utils.IPUtil;
import com.yb.aiot.utils.MyHttpUtil;
import com.yb.aiot.utils.YmlUtil;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * <p>
 *
 * <p>
 *
 * @author author
 * @date 2022/11/28 15:52
 */
public class CallbackUtil {

    private static final JSONObject aiotConf = YmlUtil.getJSONObject("aiot");

    private static final Map<String, Long> timeMap = new HashMap<>();

    /**
     * 事件推送
     *
     * @param flag   flag
     * @param cbData cbData
     */
    public static void sendCbData(boolean flag, JSONObject cbData) {
        if (!flag) {
            String picture = cbData.getString("picture").split("down")[1].substring(1);
            MyFileUtil.deleteFile(picture);
            return;
        }
        DeviceDto deviceDto = getDeviceInfo(cbData.getString("deviceIp"));
        if (ObjectUtils.isEmpty(deviceDto)) {
            return;
        }
        EventType eventType = getEventType(deviceDto, cbData.getString("name"));
        if (ObjectUtils.isEmpty(eventType)) {
            return;
        }
        EventInfo eventInfo = new EventInfo();
        eventInfo.setDeviceInfoId(deviceDto.getDeviceId());
        eventInfo.setEventTypeId(eventType.getId());
        eventInfo.setName(cbData.getString("name"));
        eventInfo.setEventTime(parseLocalDateTime(cbData.getString("time")));
        eventInfo.setHandleStatus(false);
        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(setEventDetail("事件图片", "picture", cbData.getString("picture")));
        eventInfo.setEventDetail(JSONObject.toJSONString(jsonObjectList));
        if (saveEventInfo(eventInfo)) {
            System.out.println(eventInfo);
            System.out.println();
        }
    }

    public static EventType getEventType(DeviceDto deviceDto, String eventName) {
        if (ObjectUtils.isEmpty(deviceDto)) {
            return null;
        }
        EventType eventType = new EventType();
        eventType.setProductTypeId(deviceDto.getProductTypeId());
        eventType.setProductInfoId(deviceDto.getProductInfoId());
        eventType.setName(eventName);
        String code = Base64.getEncoder().encodeToString(eventName.getBytes(StandardCharsets.UTF_8));
        eventType.setCode(code);
        eventType.setLevel(1);
        eventType.setSaveDay(30);
        return selectOrSaveEventType(eventType);
    }

    public static JSONObject setEventDetail(String name, String type, String value) {
        JSONObject eventDetail = new JSONObject();
        eventDetail.put("name", name);
        eventDetail.put("type", type);
        eventDetail.put("value", value);
        return eventDetail;
    }

    /**
     * 根据ip查询设备信息
     *
     * @param deviceIp deviceIp
     * @return entity.device.module.com.yb.aiot.DeviceInfo
     */
    public static DeviceDto getDeviceInfo(String deviceIp) {
        String url = String.format("%s/device/select/%s", IPUtil.getServeUrl(), deviceIp);
        try {
            JSONObject jsonObject = MyHttpUtil.get(url);
            return JSONObject.parseObject(jsonObject.getString("data"), DeviceDto.class);
        } catch (IORuntimeException e) {
            System.out.println("根据ip查询设备信息接口调用异常!");
            return null;
        }
    }

    public static EventType selectOrSaveEventType(EventType eventType) {
        String url = String.format("%s/device/selectOrSaveEventType", IPUtil.getServeUrl());
        try {
            JSONObject jsonObject = MyHttpUtil.post(url, JSONObject.toJSONString(eventType));
            return JSONObject.parseObject(jsonObject.getString("data"), EventType.class);
        } catch (IORuntimeException e) {
            System.out.println("根据ip查询设备信息接口调用异常!");
            return null;
        }
    }

    /**
     * saveEventInfo
     *
     * @param eventInfo eventInfo
     * @return boolean
     */
    public static boolean saveEventInfo(EventInfo eventInfo) throws IORuntimeException {
        String url = String.format("%s/device/eventInfo/save", IPUtil.getServeUrl());
        try {
            JSONObject jsonObject = MyHttpUtil.post(url, JSONObject.toJSONString(eventInfo));
            System.out.println(jsonObject);
            if (jsonObject.getInteger("code").equals(200)) {
                return true;
            }
        } catch (IORuntimeException e) {
            System.out.println("saveEventInfo接口调用异常!");
        }
        return false;
    }

    /**
     * 门禁考勤数据推送
     *
     * @param cbData cbData
     * @return void
     */
    public static void sendAttendanceCbData(JSONObject cbData) {
        JSONObject attendanceData = getAttendanceData();
        attendanceData.put("deviceIp", cbData.getString("deviceIp"));
        attendanceData.put("device", cbData.getString("door_name"));
        attendanceData.put("clockTYpe", cbData.getString("typeNum"));
        // 推送
        String url = aiotConf.getString("attendance-url");
        // 存放请求头，可以存放多个请求头
        HashMap<String, String> headers = new HashMap<>();
        headers.put("cardNo", cbData.getString("cardNo"));
        try {
            // 发送post请求并接收响应数据
            MyHttpUtil.post(url, attendanceData.toJSONString());
        } catch (IORuntimeException e) {
            System.out.println("门禁考勤数据推送接口调用异常!");
        }
    }

    /**
     * 宇视门禁测温数据推送
     *
     * @param jsonObject 宇视门禁测温数据
     */
    public static void sendUniviewData(JSONObject jsonObject) {
        JSONObject data = new JSONObject();
        data.put("time", jsonObject.getString("Timestamp"));
        data.put("deviceCode", jsonObject.getString("DeviceCode"));
        JSONObject libMatInfoList = jsonObject.getJSONArray("LibMatInfoList").getJSONObject(0);
        data.put("libID", libMatInfoList.getString("LibID"));
        JSONObject MatchPersonInfo = libMatInfoList.getJSONObject("MatchPersonInfo");
        data.put("name", MatchPersonInfo.getString("PersonName"));
        data.put("gender", MatchPersonInfo.getString("Gender"));
        data.put("cardID", MatchPersonInfo.getString("CardID"));
        data.put("identityNo", MatchPersonInfo.getString("IdentityNo"));
        JSONArray jsonArray = jsonObject.getJSONArray("FaceInfoList");
        JSONObject faceInfoList = JSON.parseObject(jsonArray.get(0).toString());
        data.put("temperature", faceInfoList.getString("Temperature"));
//        JSONObject faceImage = faceInfoList.getJSONObject("FaceImage");
//        data.put("faceImage", faceImage.getString("Data"));
        //GenerateImage(faceImage.getString("Data"));
        System.out.println(data);
        if (!"0.0".equals(faceInfoList.getString("Temperature")) && !"未识别".equals(MatchPersonInfo.getString("PersonName"))) {
            try {
                JSONObject result = MyHttpUtil.post(aiotConf.getString("uniview-event-send-url"), data.toJSONString());
                System.out.println(result);
            } catch (IORuntimeException e) {
                System.out.println("宇视门禁测温数据推送接口调用异常!");
            }
        }
    }

    /**
     * 判断事件是否需要推送
     *
     * @param cbData cbData
     * @return boolean
     */
    public static boolean isSendCbData(JSONObject cbData) {
        boolean flag = timeMap.size() == 0;
        String deviceIp = cbData.getString("deviceIp");
        if (timeMap.get(deviceIp) == null) flag = true;
        long time = parseLocalDateTime(cbData.getString("time")).toInstant(ZoneOffset.of("+8")).toEpochMilli();
        if (!flag) {
            long tempIntervalTime = time - timeMap.get(deviceIp);
            long intervalTime = aiotConf.getLong("interval-time") * 60000;
            if (tempIntervalTime > intervalTime) flag = true;
        }
        timeMap.put(deviceIp, time);
        return flag;
    }

    public static LocalDateTime parseLocalDateTime(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch (DateTimeParseException e) {
            return LocalDateTime.now();
        }
    }

    /**
     * 门禁考勤数据结构
     *
     * @param
     * @return com.alibaba.fastjson.JSONObject
     */
    public static JSONObject getAttendanceData() {
        String jSONObjectStr = "{\n" + "    \"wifi\": {\n" + "        \"wifiName\": \"1\",\n" + "        \"wifiMac\": \"1\"\n" + "    },\n" + "    \"gisInfo\": {\n" + "        \"point\": {\n" + "            \"lat\": 26,\n" + "            \"lng\": 107\n" + "        },\n" + "        \"address\": \"1111\",\n" + "    },\n" + "    \"device\": \"20\",\n" + "    \"clockTYpe\": 1,\n" + "    \"deviceIp\": 4\n" + "}";
        return JSONObject.parseObject(jSONObjectStr);
    }

}
