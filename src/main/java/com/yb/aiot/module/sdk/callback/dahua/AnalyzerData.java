package com.yb.aiot.module.sdk.callback.dahua;

import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Pointer;
import com.yb.aiot.module.common.utils.MyFileUtil;
import com.yb.aiot.module.sdk.module.dahua.DhModule;
import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;
import com.yb.aiot.module.sdk.netsdk.dahua.lib.ToolKits;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import static com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib.*;

/**
 * <p>
 * 大华事件信息分析
 * <p>
 *
 * @author author
 * @date 2022/4/29 16:14
 */
public class AnalyzerData {

    public static JSONObject analyze(LLong lAnalyzerHandle, int dwAlarmType, Pointer pAlarmInfo, Pointer pBuffer, int dwBufSize) {
        JSONObject analyzerData = null;
        String name = null;
        LocalDateTime time = null;
        String flag = null;

        switch (dwAlarmType) {
            // 警戒线事件
            case EVENT_IVS_CROSSLINEDETECTION: {
                NetSDKLib.DEV_EVENT_CROSSLINE_INFO msg = new NetSDKLib.DEV_EVENT_CROSSLINE_INFO();
                ToolKits.GetPointerData(pAlarmInfo, msg);
                name = "警戒线事件";
                flag = "camera";
                time = msg.UTC.toLocalDateTime();
                break;
            }
            // 警戒区事件
            case EVENT_IVS_CROSSREGIONDETECTION: {
                NetSDKLib.DEV_EVENT_CROSSREGION_INFO msg = new NetSDKLib.DEV_EVENT_CROSSREGION_INFO();
                ToolKits.GetPointerData(pAlarmInfo, msg);
                name = "警戒区事件";
                flag = "camera";
                time = msg.UTC.toLocalDateTime();
                break;
            }
            // 物品遗留事件
            case EVENT_IVS_LEFTDETECTION: {
                DEV_EVENT_LEFT_INFO msg = new DEV_EVENT_LEFT_INFO();
                ToolKits.GetPointerData(pAlarmInfo, msg);
                name = "物品遗留事件";
                flag = "camera";
                time = msg.UTC.toLocalDateTime();
                break;
            }
            // 停留事件
            case EVENT_IVS_STAYDETECTION: {
                DEV_EVENT_STAY_INFO msg = new DEV_EVENT_STAY_INFO();
                ToolKits.GetPointerData(pAlarmInfo, msg);
                name = "停留事件";
                flag = "camera";
                time = msg.UTC.toLocalDateTime();
                break;
            }
            // 徘徊事件
            case EVENT_IVS_WANDERDETECTION: {
                DEV_EVENT_WANDER_INFO msg = new DEV_EVENT_WANDER_INFO();
                ToolKits.GetPointerData(pAlarmInfo, msg);
                name = "徘徊事件";
                flag = "camera";
                time = msg.UTC.toLocalDateTime();
                break;
            }

            // 移动事件
            case EVENT_IVS_MOVEDETECTION: {
                DEV_EVENT_MOVE_INFO msg = new DEV_EVENT_MOVE_INFO();
                ToolKits.GetPointerData(pAlarmInfo, msg);
                name = "移动事件";
                flag = "camera";
                time = msg.UTC.toLocalDateTime();
                break;
            }
            // 聚众事件
            case EVENT_IVS_RIOTERDETECTION: {
                DEV_EVENT_RIOTERL_INFO msg = new DEV_EVENT_RIOTERL_INFO();
                ToolKits.GetPointerData(pAlarmInfo, msg);
                name = "聚众事件";
                flag = "camera";
                time = msg.UTC.toLocalDateTime();
                break;
            }
            // 斗殴事件
            case EVENT_IVS_FIGHTDETECTION: {
                DEV_EVENT_FIGHT_INFO msg = new DEV_EVENT_FIGHT_INFO();
                ToolKits.GetPointerData(pAlarmInfo, msg);
                name = "斗殴事件";
                flag = "camera";
                time = msg.UTC.toLocalDateTime();
                break;
            }
            // 门禁事件 (对应 DEV_EVENT_ACCESS_CTL_INFO)
            case EVENT_IVS_ACCESS_CTL: {
                DEV_EVENT_ACCESS_CTL_INFO msg = new DEV_EVENT_ACCESS_CTL_INFO();
                name = openMethod(msg.emOpenMethod);
                name += msg.nErrorCode == 0 ? "成功" : "失败";
                int emEventType = msg.emEventType;
                ToolKits.GetPointerData(pAlarmInfo, msg);
                time = msg.UTC.toLocalDateTime();
                flag = "entrance";
                break;
            }
        }
        String pictureName;
        if (name != null) {
            pictureName = MyFileUtil.makeFileName(flag);
            try {
                ToolKits.savePicture(pBuffer, 0, dwBufSize, MyFileUtil.getAbsolutePath(pictureName));
            } catch (Exception e) {
                e.printStackTrace();
            }
            analyzerData = new JSONObject(new LinkedHashMap<>());
            analyzerData.put("flag", flag);
            analyzerData.put("name", name);
            analyzerData.put("deviceIp", DhModule.callbackMap.get(lAnalyzerHandle));
            analyzerData.put("time", time);
            analyzerData.put("picture", MyFileUtil.getDownFileUrl(pictureName));
        }
        return analyzerData;
    }

    /**
     * 获取门禁事件类型
     *
     * @param emOpenMethod emOpenMethod
     * @return java.lang.String
     */
    public static String openMethod(int emOpenMethod) {
        String name;
        switch (emOpenMethod) {
            case 0: {
                name = "远程开门";
                break;
            }
            case 1: {
                name = "密码开门";
                break;
            }
            case 2: {
                name = "刷卡开门";
                break;
            }
            case 3: {
                name = "先刷卡后密码开门";
                break;
            }
            case 4: {
                name = "先密码后刷卡开门";
                break;
            }
            case 5: {
                name = "远程开门(通过室内机或者平台对门口机开门)";
                break;
            }
            case 6: {
                name = "开门按钮进行开门";
                break;
            }
            case 7: {
                name = "指纹开门";
                break;
            }
            case 8: {
                name = "密码+刷卡+指纹组合开门";
                break;
            }
            case 10: {
                name = "密码+指纹组合开门";
                break;
            }
            case 11: {
                name = "刷卡+指纹组合开门";
                break;
            }
            case 12: {
                name = "多人开门";
                break;
            }
            case 13: {
                name = "钥匙开门";
                break;
            }
            case 14: {
                name = "胁迫密码开门";
                break;
            }
            case 15: {
                name = "二维码开门";
                break;
            }
            case 16: {
                name = "人脸识别开门";
                break;
            }
            case 18: {
                name = "人证对比开门";
                break;
            }
            case 19: {
                name = "身份证+人证比对开门";
                break;
            }
            case 20: {
                name = "蓝牙开门";
                break;
            }
            case 21: {
                name = "个性化密码开门";
                break;
            }
            case 22: {
                name = "UserID+密码开门";
                break;
            }
            case 23: {
                name = "人脸+密码开门";
                break;
            }
            case 24: {
                name = "指纹+密码开门";
                break;
            }
            case 25: {
                name = "指纹+人脸开门";
                break;
            }
            case 26: {
                name = "刷卡+人脸开门";
                break;
            }
            case 27: {
                name = "人脸或密码开门";
                break;
            }
            case 28: {
                name = "指纹或密码开门";
                break;
            }
            case 29: {
                name = "指纹或人脸开门";
                break;
            }
            case 30: {
                name = "刷卡或人脸开门";
                break;
            }
            case 31: {
                name = "刷卡或指纹开门";
                break;
            }
            case 32: {
                name = "指纹+人脸+密码开门";
                break;
            }
            case 33: {
                name = "刷卡+人脸+密码开门";
                break;
            }
            case 34: {
                name = "刷卡+指纹+密码开门";
                break;
            }
            case 35: {
                name = "卡+指纹+人脸组合开门";
                break;
            }
            case 36: {
                name = "指纹或人脸或密码";
                break;
            }
            case 37: {
                name = "卡或人脸或密码开门";
                break;
            }
            case 38: {
                name = "卡或指纹或人脸开门";
                break;
            }
            case 39: {
                name = "卡+指纹+人脸+密码组合开门";
                break;
            }
            case 40: {
                name = "卡或指纹或人脸或密码开门";
                break;
            }
            case 41: {
                name = "(身份证+人证比对)或刷卡或人脸开门";
                break;
            }
            case 42: {
                name = "人证比对或刷卡(二维码)或人脸开门";
                break;
            }
            case 43: {
                name = "DTMF开门(包括SIPINFO,RFC2833,INBAND)";
                break;
            }
            case 44: {
                name = "远程二维码开门";
                break;
            }
            case 45: {
                name = "远程人脸开门";
                break;
            }
            case 46: {
                name = "人证比对开门(指纹)";
                break;
            }
            default: {
                name = "未知开门方式";
            }
        }
        return name;
    }

}
