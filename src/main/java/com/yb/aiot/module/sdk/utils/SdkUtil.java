package com.yb.aiot.module.sdk.utils;

import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yb.aiot.module.sdk.dto.DeviceDto;
import com.yb.aiot.module.sdk.module.SdkModule;
import com.yb.aiot.module.sdk.module.dahua.DhModule;
import com.yb.aiot.module.sdk.module.hik.HikModule;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * SDK工具类
 * <p>
 *
 * @author author
 * @date 2022/6/16 10:00
 */

public class SdkUtil {

    private static final DhModule dhModule = new DhModule();

    private static final HikModule hikModule = new HikModule();

    /**
     * 加载sdk
     */
    public static void loadSdk() {
        DhModule.createSDKInstance();
        HikModule.createSDKInstance();
    }

    /**
     * 加载sdk
     *
     * @param manufacturer 厂家
     */
    public static boolean loadSdk(String manufacturer) {
        boolean flag = false;
        switch (manufacturer) {
            case "HK":
                flag = HikModule.createSDKInstance();
                break;
            case "DH":
                flag = DhModule.createSDKInstance();
                break;
        }
        return flag;
    }

    /**
     * 获取sdk Module
     *
     * @param manufacturer manufacturer
     * @return module.sdk.module.com.yb.aiot.SdkModule
     */
    public static SdkModule getModule(String manufacturer) {
        SdkModule module = null;
        switch (manufacturer) {
            case "HK":
                module = hikModule;
                break;
            case "DH":
                module = dhModule;
                break;
        }
        return module;
    }

    /**
     * 获取指定文件路径
     *
     * @param sdkName  sdkName
     * @param fileName 文件名
     * @return java.lang.String
     */
    public static String getSdkPath(String sdkName, String fileName) {
        String sdkPath = System.getProperty("user.dir");
        sdkPath += "/sdklib/" + sdkName;
        OsInfo osInfo = SystemUtil.getOsInfo();
        if (osInfo.isLinux()) {
            sdkPath += "/linux/" + fileName;
        } else {
            sdkPath += "/windows/" + fileName;
            sdkPath = sdkPath.replace("/", osInfo.getFileSeparator());
        }
        return sdkPath;
    }

    /**
     * 将json数据解析为DeviceDto
     *
     * @param jsonObject json数据
     * @return dto.sdk.module.com.yb.aiot.DeviceDto
     */
    public static DeviceDto makeDeviceDto(JSONObject jsonObject) {
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setIp(jsonObject.getString("ip"));
        JSONObject command = jsonObject.getJSONObject("command");
        String functionId = command.getString("functionId");
        deviceDto.setMethod(functionId);
        JSONArray jsonArray = command.getJSONArray("inputs");
        List<JSONObject> jsonObjectList = JSONObject.parseArray(jsonArray.toJSONString(), JSONObject.class);
        for (JSONObject param : jsonObjectList) {
            switch (param.getString("id")) {
                case "ptzCommand":
                    deviceDto.setPtzCommand(param.getInteger("value"));
                    break;
                case "startOrStop":
                    deviceDto.setStartOrStop(param.getInteger("value"));
                    break;
                case "controlCommand":
                    deviceDto.setControlCommand(param.getInteger("value"));
                    break;
                case "cameraType":
                    SdkModule sdkModule = getModule(param.getString("value"));
                    deviceDto.setSdkModule(sdkModule);
                    break;
                case "streamAddr":
                    deviceDto.setStreamAddr(param.getString("value"));
                    break;
            }
        }
        return deviceDto;
    }

    /**
     * 获取sdk厂商
     *
     * @param thingsModelStr 物模型json格式
     * @return java.lang.String
     */
    public static String getManufacturer(String thingsModelStr) {
        if (ObjectUtils.isEmpty(thingsModelStr)) {
            return null;
        }
        JSONObject thingsModel;
        try {
            thingsModel = JSONObject.parseObject(thingsModelStr);
        } catch (JSONException e) {
            return null;
        }
        JSONArray jsonArray = thingsModel.getJSONArray("properties");
        if (ObjectUtils.isEmpty(jsonArray) && jsonArray.size() == 0) {
            return null;
        }
        List<JSONObject> jsonObjectList = JSONObject.parseArray(jsonArray.toJSONString(), JSONObject.class);
        String manufacturer = null;
        for (JSONObject param : jsonObjectList) {
            if (param.getString("id").equals("cameraType")) {
                manufacturer = param.getString("value");
            }
        }
        return manufacturer;
    }

    /**
     * 海康SDK时间解析
     *
     * @param time time
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime parseTime(int time) {
        int year = (time >> 26) + 2000;
        int month = (time >> 22) & 15;
        int day = (time >> 17) & 31;
        int hour = (time >> 12) & 31;
        int min = (time >> 6) & 63;
        int second = (time) & 63;
        return LocalDateTime.of(year, month, day, hour, min, second);
    }

}
