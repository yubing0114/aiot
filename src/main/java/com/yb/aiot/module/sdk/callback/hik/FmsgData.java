package com.yb.aiot.module.sdk.callback.hik;

import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Pointer;
import com.yb.aiot.module.common.utils.MyFileUtil;
import com.yb.aiot.module.sdk.netsdk.hik.HCNetSDK;
import com.yb.aiot.module.sdk.netsdk.hik.HikStructure;
import com.yb.aiot.module.sdk.utils.SdkUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import static com.yb.aiot.module.sdk.netsdk.hik.HCNetSDK.*;

/**
 * <p>
 * 海康事件信息分析
 * <p>
 *
 * @author author
 * @date 2022/5/6 16:53
 */
public class FmsgData {

    /**
     * 事件信息分析
     *
     * @param lCommand
     * @param pAlarmer
     * @param pAlarmInfo
     * @param dwBufLen
     * @param pUser
     * @return com.alibaba.fastjson.JSONObject
     */
    public static JSONObject AlarmDataHandle(int lCommand, NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        JSONObject analyzerData = null;
        // lCommand是传的报警类型
        switch (lCommand) {
            //行为分析信息
            case COMM_ALARM_RULE:
                analyzerData = commAlarmRule(pAlarmer, pAlarmInfo);
                break;
            // 门禁主机报警信息
            case COMM_ALARM_ACS:
                analyzerData = commAlarmAcs(pAlarmer, pAlarmInfo);
                break;
            // 交通抓拍的终端图片上传
            case COMM_ITS_PLATE_RESULT:
                analyzerData = commItsPlateResult(pAlarmInfo, pAlarmer);
                break;
        }
        return analyzerData;
    }

    /**
     * 行为分析信息
     *
     * @param pAlarmer
     * @param pAlarmInfo
     * @return com.alibaba.fastjson.JSONObject
     */
    public static JSONObject commAlarmRule(NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo) {
        // 读取事件信息
        NET_VCA_RULE_ALARM msg = new NET_VCA_RULE_ALARM();
        getPointerData(pAlarmInfo, msg);
        // ip
        String deviceIp = new String(pAlarmer.sDeviceIP).trim();
        // 图片
        String pictureName = null;
        if ((msg.dwPicDataLen > 0) && (msg.byPicTransType == 0)) {
            try {
                pictureName = MyFileUtil.makeFileName("camera");
                FileOutputStream fout = new FileOutputStream(MyFileUtil.getAbsolutePath(pictureName));
                //将字节写入文件
                ByteBuffer buffers = msg.pImage.getByteBuffer(0, msg.dwPicDataLen);
                buffers.rewind();
                byte[] bytes = new byte[msg.dwPicDataLen];
                buffers.get(bytes);
                fout.write(bytes);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 写入结果数据
        JSONObject analyzerData = new JSONObject(new LinkedHashMap<>());
        analyzerData.put("flag", "camera");
        // 获取事件名称
        String name = wEventTypeEx(msg.struRuleInfo.wEventTypeEx);
        analyzerData.put("name", name);
        analyzerData.put("deviceIp", deviceIp);
        // 时间时间
        analyzerData.put("time", SdkUtil.parseTime(msg.dwAbsTime));
        analyzerData.put("picture", MyFileUtil.getDownFileUrl(pictureName));
        return analyzerData;
    }

    /**
     * 获取行为事件类型
     *
     * @param wEventTypeEx
     * @return java.lang.String
     */
    public static String wEventTypeEx(short wEventTypeEx) {
        String name;
        switch (wEventTypeEx) {
            case 1: //穿越警戒面 (越界侦测)
                name = "目标穿越警戒面报警发生";
                break;
            case 2: //目标进入区域
                name = "目标进入区域报警发生";
                break;
            case 3: //目标离开区域
                name = "目标离开区域报警发生";
                break;
            case 4: //周界入侵
                name = "周界入侵报警发生";
                break;
            case 5: //徘徊
                name = "徘徊事件触发";
                break;
            case 8: //快速移动(奔跑)，
                name = "快速移动(奔跑)事件触发";
                break;
            case 15: //离岗
                name = "离岗事件触发";
                break;
            case 20: //倒地检测
                name = "倒地事件触发";
                break;
            case 44: //玩手机
                name = "玩手机报警发生";
                break;
            case 45: //持续检测
                name = "持续检测事件触发";
                break;
            default:
                name = "其他事件";
                break;
        }
        return name;
    }

    /**
     * 门禁主机报警信息
     *
     * @param pAlarmer   报警信息
     * @param pAlarmInfo
     * @return com.alibaba.fastjson.JSONObject
     */
    private static JSONObject commAlarmAcs(NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo) {
        // 读取事件信息
        HCNetSDK.NET_DVR_ACS_ALARM_INFO msg = new HCNetSDK.NET_DVR_ACS_ALARM_INFO();
        getPointerData(pAlarmInfo, msg);
        // 设备ip
        String deviceIp = new String(pAlarmer.sDeviceIP).trim();
        // 图片
        String pictureName = null;
        if (msg.dwPicDataLen > 0) {
            try {
                pictureName = MyFileUtil.makeFileName("entrance");
                FileOutputStream fout = new FileOutputStream(MyFileUtil.getAbsolutePath(pictureName));
                // 将字节写入文件
                ByteBuffer buffers = msg.pPicData.getByteBuffer(0, msg.dwPicDataLen);
                buffers.rewind();
                byte[] bytes = new byte[msg.dwPicDataLen];
                buffers.get(bytes);
                fout.write(bytes);
                fout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 写入结果数据
        JSONObject analyzerData = new JSONObject(new LinkedHashMap<>());
        analyzerData.put("flag", "entrance");
        // 事件名称
        analyzerData.put("name", getCommAlarmAcs(msg.dwMajor, msg.dwMinor));
        analyzerData.put("deviceIp", deviceIp);
        // 时间
        analyzerData.put("time", msg.struTime.toStringTime());
        // 卡号
        analyzerData.put("cardNo", new String(msg.struAcsEventInfo.byCardNo).trim());
        // 卡类型
        analyzerData.put("cardType", msg.struAcsEventInfo.byCardType);
        // 卡号
        analyzerData.put("employeeNo", msg.struAcsEventInfo.dwEmployeeNo);
        // 图片
        analyzerData.put("picture", MyFileUtil.getDownFileUrl(pictureName));
        analyzerData.put("status", 1);
        // 温度信息（如果设备支持测温功能，人脸温度信息从NET_DVR_ACS_EVENT_INFO_EXTEND_V20结构体获取）
        if (msg.byAcsEventInfoExtendV20 == 1) {
            HCNetSDK.NET_DVR_ACS_EVENT_INFO_EXTEND_V20 strAcsInfoExV20 = new HCNetSDK.NET_DVR_ACS_EVENT_INFO_EXTEND_V20();
            getPointerData(pAlarmInfo, strAcsInfoExV20);
            analyzerData.put(" temperature", strAcsInfoExV20.fCurrTemperature);
        }
        // 考勤状态
        if (msg.byAcsEventInfoExtend == 1) {
            HCNetSDK.NET_DVR_ACS_EVENT_INFO_EXTEND strAcsInfoEx = new HCNetSDK.NET_DVR_ACS_EVENT_INFO_EXTEND();
            getPointerData(pAlarmInfo, strAcsInfoEx);
            analyzerData.put("attendanceStatus", strAcsInfoEx.byAttendanceStatus);
            analyzerData.put("employeeNo2", new String(strAcsInfoEx.byEmployeeNo).trim());
        }
        return analyzerData;
    }

    /**
     * 获取门禁事件类型
     *
     * @param dwMajor 事件朱类型
     * @param dwMinor 事件次类型
     * @return java.lang.String
     */
    private static String getCommAlarmAcs(int dwMajor, int dwMinor) {
        String sAlarmType = null;
        switch (dwMajor) {
            case 0x1:
                switch (dwMinor) {
                    case 0x404:
                        sAlarmType = "设备防拆";
                        break;
                    case 0x405:
                        sAlarmType = "设备防拆恢复";
                        break;
                    case 0x406:
                        sAlarmType = "读卡器防拆";
                        break;
                    case 0x407:
                        sAlarmType = "读卡器防拆恢复";
                        break;
                    case 0x408:
                        sAlarmType = "事件输入";
                        break;
                    case 0x409:
                        sAlarmType = "事件输入恢复";
                        break;
                    case 0x40a:
                        sAlarmType = "胁迫报警";
                        break;
                    case 0x40b:
                        sAlarmType = "离线事件满90%";
                        break;
                    case 0x40c:
                        sAlarmType = "卡号认证失败超次";
                        break;
                    case 0x40d:
                        sAlarmType = "SD卡存储满";
                        break;
                    case 0x40f:
                        sAlarmType = "门控安全模块防拆";
                        break;
                    case 0x410:
                        sAlarmType = "门控安全模块防拆恢复";
                        break;
                    default:
                        sAlarmType = dwMinor + "未知次类型";
                        break;
                }
                break;
            case 0x2:
                switch (dwMinor) {
                    case 0x27:
                        sAlarmType = "网络断开";
                        break;
                    case 0x400:
                        sAlarmType = "设备上电启动";
                        break;
                    case 0x407:
                        sAlarmType = "网络恢复";
                        break;
                    case 0x408:
                        sAlarmType = "FLASH读写异常";
                        break;
                    case 0x409:
                        sAlarmType = "读卡器掉线";
                        break;
                    case 0x40a:
                        sAlarmType = "读卡器掉线恢复";
                        break;
                    case 0x40f:
                        sAlarmType = "门控安全模块掉线";
                        break;
                    case 0x410:
                        sAlarmType = "门控安全模块在线";
                        break;
                    case 0x41d:
                        sAlarmType = "身份证阅读器未连接";
                        break;
                    case 0x41e:
                        sAlarmType = "身份证阅读器连接恢复";
                        break;
                    case 0x423:
                        sAlarmType = "COM口未连接";
                        break;
                    case 0x424:
                        sAlarmType = "COM口连接恢复";
                        break;
                    case 0x426:
                        sAlarmType = "人证设备在线";
                        break;
                    case 0x427:
                        sAlarmType = "人证设备离线";
                        break;
                    case 0x428:
                        sAlarmType = "本地登录锁定";
                        break;
                    case 0x429:
                        sAlarmType = "本地登录解锁";
                        break;
                    default:
                        sAlarmType = dwMinor + "未知次类型";
                        break;
                }
                break;
            case 0x3:
                switch (dwMinor) {
                    case 0x50:
                        sAlarmType = "本地登陆";
                        break;
                    case 0x5a:
                        sAlarmType = "本地升级";
                        break;
                    case 0x70:
                        sAlarmType = "远程登录";
                        break;
                    case 0x71:
                        sAlarmType = "远程注销登陆";
                        break;
                    case 0x79:
                        sAlarmType = "远程布防";
                        break;
                    case 0x7a:
                        sAlarmType = "远程撤防";
                        break;
                    case 0x7b:
                        sAlarmType = "远程重启";
                        break;
                    case 0x7e:
                        sAlarmType = "远程升级";
                        break;
                    case 0x86:
                        sAlarmType = "远程导出配置文件";
                        break;
                    case 0x87:
                        sAlarmType = "远程导入配置文件";
                        break;
                    case 0x400:
                        sAlarmType = "远程开门";
                        break;
                    case 0x401:
                        sAlarmType = "远程关门(受控)";
                        break;
                    case 0x402:
                        sAlarmType = "远程常开(自由)";
                        break;
                    case 0x403:
                        sAlarmType = "远程常关(禁用)";
                        break;
                    case 0x404:
                        sAlarmType = "远程手动校时";
                        break;
                    case 0x405:
                        sAlarmType = "NTP自动校时";
                        break;
                    case 0x406:
                        sAlarmType = "远程清空卡号";
                        break;
                    case 0x407:
                        sAlarmType = "远程恢复默认参数";
                        break;
                    case 0x40a:
                        sAlarmType = "本地恢复默认参数";
                        break;
                    case 0x40b:
                        sAlarmType = "远程抓拍";
                        break;
                    case 0x40c:
                        sAlarmType = "修改网络中心参数配置";
                        break;
                    case 0x40e:
                        sAlarmType = "修改中心组参数配置";
                        break;
                    case 0x40f:
                        sAlarmType = "解除码输入";
                        break;
                    case 0x419:
                        sAlarmType = "远程实时布防";
                        break;
                    case 0x41a:
                        sAlarmType = "远程实时撤防";
                        break;
                    default:
                        sAlarmType = dwMinor + "未知次类型";
                        break;
                }
                break;
            case 0x5:
                switch (dwMinor) {
                    case 0x01:
                        sAlarmType = "合法卡认证通过";
                        break;
                    case 0x02:
                        sAlarmType = "刷卡加密码认证通过";
                        break;
                    case 0x03:
                        sAlarmType = "刷卡加密码认证失败";
                        break;
                    case 0x04:
                        sAlarmType = "数卡加密码认证超时";
                        break;
                    case 0x05:
                        sAlarmType = "刷卡加密码超次";
                        break;
                    case 0x06:
                        sAlarmType = "未分配权限";
                        break;
                    case 0x07:
                        sAlarmType = "无效时段";
                        break;
                    case 0x08:
                        sAlarmType = "卡号过期";
                        break;
                    case 0x09:
                        sAlarmType = "无此卡号";
                        break;
                    case 0x0a:
                        sAlarmType = "反潜回认证失败";
                        break;
                    case 0x0c:
                        sAlarmType = "卡不属于多重认证群组";
                        break;
                    case 0x0d:
                        sAlarmType = "卡不在多重认证时间段内";
                        break;
                    case 0x0e:
                        sAlarmType = "多重认证模式超级权限认证失败";
                        break;
                    case 0x0f:
                        sAlarmType = "多重认证模式远程认证失败";
                        break;
                    case 0x10:
                        sAlarmType = "多重认证成功";
                        break;
                    case 0x11:
                        sAlarmType = "首卡开门开始";
                        break;
                    case 0x12:
                        sAlarmType = "首卡开门结束";
                        break;
                    case 0x13:
                        sAlarmType = "常开状态开始";
                        break;
                    case 0x14:
                        sAlarmType = "常开状态结束";
                        break;
                    case 0x15:
                        sAlarmType = "门锁打开";
                        break;
                    case 0x16:
                        sAlarmType = "门锁关闭";
                        break;
                    case 0x17:
                        sAlarmType = "开门按钮打开";
                        break;
                    case 0x18:
                        sAlarmType = "开门按钮放开";
                        break;
                    case 0x19:
                        sAlarmType = "正常开门(门磁)";
                        break;
                    case 0x1a:
                        sAlarmType = "正常关门(门磁)";
                        break;
                    case 0x1b:
                        sAlarmType = "门异常打开(门磁)";
                        break;
                    case 0x1c:
                        sAlarmType = "门打开超时(门磁)";
                        break;
                    case 0x1d:
                        sAlarmType = "报警输出打开";
                        break;
                    case 0x1e:
                        sAlarmType = "报警输出关闭";
                        break;
                    case 0x1f:
                        sAlarmType = "常关状态开始";
                        break;
                    case 0x20:
                        sAlarmType = "常关状态结束";
                        break;
                    case 0x21:
                        sAlarmType = "多重多重认证需要远程开门";
                        break;
                    case 0x22:
                        sAlarmType = "多重认证超级密码认证成功事件";
                        break;
                    case 0x23:
                        sAlarmType = "多重认证重复认证事件";
                        break;
                    case 0x24:
                        sAlarmType = "多重认证重复认证事件";
                        break;
                    case 0x25:
                        sAlarmType = "门铃响";
                        break;
                    case 0x26:
                        sAlarmType = "指纹比对通过";
                        break;
                    case 0x27:
                        sAlarmType = "指纹比对失败";
                        break;
                    case 0x28:
                        sAlarmType = "刷卡加指纹认证通过";
                        break;
                    case 0x29:
                        sAlarmType = "刷卡加指纹认证失败";
                        break;
                    case 0x2a:
                        sAlarmType = "刷卡加指纹认证超时";
                        break;
                    case 0x2b:
                        sAlarmType = "刷卡加指纹加密码认证通过";
                        break;
                    case 0x2c:
                        sAlarmType = "刷卡加指纹加密码认证失败";
                        break;
                    case 0x2d:
                        sAlarmType = "刷卡加指纹加密码认证超时";
                        break;
                    case 0x2e:
                        sAlarmType = "指纹加密码认证通过";
                        break;
                    case 0x2f:
                        sAlarmType = "指纹加密码认证失败";
                        break;
                    case 0x30:
                        sAlarmType = "指纹加密码认证超时";
                        break;
                    case 0x31:
                        sAlarmType = "指纹不存在";
                        break;
                    case 0x33:
                        sAlarmType = "呼叫中心事件";
                        break;
                    case 0x36:
                        sAlarmType = "人脸加指纹认证通过";
                        break;
                    case 0x37:
                        sAlarmType = "人脸加指纹认证失败";
                        break;
                    case 0x38:
                        sAlarmType = "人脸加指纹认证超时";
                        break;
                    case 0x39:
                        sAlarmType = "人脸加密码认证通过";
                        break;
                    case 0x3a:
                        sAlarmType = "人脸加密码认证失败";
                        break;
                    case 0x3b:
                        sAlarmType = "人脸加密码认证超时";
                        break;
                    case 0x3c:
                        sAlarmType = "人脸加刷卡认证通过";
                        break;
                    case 0x3d:
                        sAlarmType = "人脸加刷卡认证失败";
                        break;
                    case 0x3e:
                        sAlarmType = "人脸加刷卡认证超时";
                        break;
                    case 0x3f:
                        sAlarmType = "人脸加密码加指纹认证通过";
                        break;
                    case 0x40:
                        sAlarmType = "人脸加密码加指纹认证失败";
                        break;
                    case 0x41:
                        sAlarmType = "人脸加密码加指纹认证超时";
                        break;
                    case 0x42:
                        sAlarmType = "人脸加刷卡加指纹认证通过";
                        break;
                    case 0x43:
                        sAlarmType = "人脸加刷卡加指纹认证失败";
                        break;
                    case 0x44:
                        sAlarmType = "人脸加刷卡加指纹认证超时";
                        break;
                    case 0x45:
                        sAlarmType = "工号加指纹认证通过";
                        break;
                    case 0x46:
                        sAlarmType = "工号加指纹认证失败";
                        break;
                    case 0x47:
                        sAlarmType = "工号加指纹认证超时";
                        break;
                    case 0x48:
                        sAlarmType = "工号加指纹加密码认证通过";
                        break;
                    case 0x49:
                        sAlarmType = "工号加指纹加密码认证失败";
                        break;
                    case 0x4a:
                        sAlarmType = "工号加指纹加密码认证超时";
                        break;
                    case 0x4b:
                        sAlarmType = "人脸认证通过";
                        break;
                    case 0x4c:
                        sAlarmType = "人脸认证失败";
                        break;
                    case 0x4d:
                        sAlarmType = "工号加人脸认证通过";
                        break;
                    case 0x4e:
                        sAlarmType = "工号加人脸认证失败";
                        break;
                    case 0x4f:
                        sAlarmType = "工号加人脸认证超时";
                        break;
                    case 0x50:
                        sAlarmType = "人脸识别失败";
                        break;
                    case 0x51:
                        sAlarmType = "首卡授权开始";
                        break;
                    case 0x52:
                        sAlarmType = "首卡授权结束";
                        break;
                    case 0x65:
                        sAlarmType = "工号加密码认证通过";
                        break;
                    case 0x66:
                        sAlarmType = "工号加密码认证失败";
                        break;
                    case 0x67:
                        sAlarmType = "工号加密码认证超时";
                        break;
                    case 0x68:
                        sAlarmType = "真人检测失败";
                        break;
                    case 0x69:
                        sAlarmType = "人证比对通过";
                        break;
                    case 0x70:
                        sAlarmType = "人证比对失败";
                        break;
                    case 0x71:
                        sAlarmType = "禁止名单事件";
                        break;
                    default:
                        sAlarmType = dwMinor + "未知次类型";
                        break;
                }
                break;
        }
        return sAlarmType;
    }

    /**
     * 交通抓拍结果报警信息
     *
     * @param pAlarmInfo
     * @param pAlarmer
     * @return com.alibaba.fastjson.JSONObject
     */
    private static JSONObject commItsPlateResult(Pointer pAlarmInfo, NET_DVR_ALARMER pAlarmer) {
        JSONObject analyzerData = new JSONObject(new LinkedHashMap<>());
        // 读取事件信息
        NET_ITS_PLATE_RESULT msg = new NET_ITS_PLATE_RESULT();
        getPointerData(pAlarmInfo, msg);
        analyzerData.put("flag", "parking");
        // 车牌号码
        String srtPlate;
        try {
            srtPlate = new String(msg.struPlateInfo.sLicense, "GBK").trim();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        analyzerData.put("licensePlate", srtPlate.substring(1));
        // 车辆类型 0 表示其它车型，1 表示小型车，2 表示大型车 ,3表示行人触发 ,4表示二轮车触发 5表示三轮车触发(3.5Ver)
        int byVehicleType = msg.struVehicleInfo.byVehicleType;
        analyzerData.put("vehicleType", getVehicleType(byVehicleType));
        // 监测点编号
        String monitoringSiteId = new String(msg.byMonitoringSiteID).trim();
        analyzerData.put("monitoringSiteID", monitoringSiteId);
        // 设备ip
        String deviceIp = new String(pAlarmer.sDeviceIP).trim();
        analyzerData.put("deviceIp", deviceIp);
        // 时间
        analyzerData.put("time", msg.struSnapFirstPicTime.toStringTime());
        // 图片
        String pictureName = null;
        for (int i = 0; i < msg.dwPicNum; i++) {
            // i=1(vehiclePicture车辆图片),i=2(licensePlatePicture车牌图片)
            if (i == 1) {
                if (msg.struPicInfo[i].dwDataLen > 0) {
                    try {
                        pictureName = MyFileUtil.makeFileName("parking");
                        FileOutputStream fout = new FileOutputStream(MyFileUtil.getAbsolutePath(pictureName));
                        // 将字节写入文件
                        ByteBuffer buffers = msg.struPicInfo[i].pBuffer.getByteBuffer(0, msg.struPicInfo[i].dwDataLen);
                        buffers.rewind();
                        byte[] bytes = new byte[msg.struPicInfo[i].dwDataLen];
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                analyzerData.put("picture", MyFileUtil.getDownFileUrl(pictureName));
            }
        }
        return analyzerData;
    }

    /**
     * 获取车辆类型
     *
     * @param typeNumber
     * @return java.lang.String
     */
    private static String getVehicleType(int typeNumber) {
        String type = null;
        //车辆类型 0 表示其它车型，1 表示小型车，2 表示大型车 ,3表示行人触发 ,4表示二轮车触发 5表示三轮车触发(3.5Ver)
        switch (typeNumber) {
            case 0:
                type = "其它车型";
                break;
            case 1:
                type = "小型车";
                break;
            case 2:
                type = "大型车";
                break;
            case 3:
                type = "行人触发";
                break;
            case 4:
                type = "二轮车触发";
                break;
            case 5:
                type = "三轮车触发";
                break;
        }
        return type;
    }

    /**
     * 读取事件信息
     *
     * @param pAlarmInfo
     * @param msg
     * @return void
     */
    public static void getPointerData(Pointer pAlarmInfo, HikStructure msg) {
        msg.write();
        msg.getPointer().write(0, pAlarmInfo.getByteArray(0, msg.size()), 0, msg.size());
        msg.read();
    }


    public static JSONObject AlarmDataHandle1(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        System.out.println("报警事件类型： lCommand:" + Integer.toHexString(lCommand));
        String MonitoringSiteID;
        String name;
        JSONObject analyzerData = new JSONObject(new LinkedHashMap<>());
        //lCommand是传的报警类型
        switch (lCommand) {
            case HCNetSDK.COMM_UPLOAD_FACESNAP_RESULT: //实时人脸抓拍上传
                System.out.println("UPLOAD_FACESNAP_Alarm");
                HCNetSDK.NET_VCA_FACESNAP_RESULT strFaceSnapInfo = new HCNetSDK.NET_VCA_FACESNAP_RESULT();
                strFaceSnapInfo.write();
                Pointer pFaceSnapInfo = strFaceSnapInfo.getPointer();
                pFaceSnapInfo.write(0, pAlarmInfo.getByteArray(0, strFaceSnapInfo.size()), 0, strFaceSnapInfo.size());
                strFaceSnapInfo.read();
                //事件时间
                int dwYear = (strFaceSnapInfo.dwAbsTime >> 26) + 2000;
                int dwMonth = (strFaceSnapInfo.dwAbsTime >> 22) & 15;
                int dwDay = (strFaceSnapInfo.dwAbsTime >> 17) & 31;
                int dwHour = (strFaceSnapInfo.dwAbsTime >> 12) & 31;
                int dwMinute = (strFaceSnapInfo.dwAbsTime >> 6) & 63;
                int dwSecond = (strFaceSnapInfo.dwAbsTime >> 0) & 63;
                String strAbsTime = "" + String.format("%04d", dwYear) + String.format("%02d", dwMonth) + String.format("%02d", dwDay) + String.format("%02d", dwHour) + String.format("%02d", dwMinute) + String.format("%02d", dwSecond);
                //人脸属性信息
                String sFaceAlarmInfo = "Abs时间:" + strAbsTime + ",年龄:" + strFaceSnapInfo.struFeature.byAge + ",性别：" + strFaceSnapInfo.struFeature.bySex + ",是否戴口罩：" + strFaceSnapInfo.struFeature.byMask + ",是否微笑：" + strFaceSnapInfo.struFeature.bySmile;
                System.out.println("人脸信息：" + sFaceAlarmInfo);
                //人脸测温信息
                if (strFaceSnapInfo.byAddInfo == 1) {
                    HCNetSDK.NET_VCA_FACESNAP_ADDINFO strAddInfo = new HCNetSDK.NET_VCA_FACESNAP_ADDINFO();
                    strAddInfo.write();
                    Pointer pAddInfo = strAddInfo.getPointer();
                    pAddInfo.write(0, strFaceSnapInfo.pAddInfoBuffer.getByteArray(0, strAddInfo.size()), 0, strAddInfo.size());
                    strAddInfo.read();
                    String sTemperatureInfo = "测温是否开启：" + strAddInfo.byFaceSnapThermometryEnabled + "人脸温度：" + strAddInfo.fFaceTemperature + "温度是否异常" + strAddInfo.byIsAbnomalTemperature + "报警温度阈值：" + strAddInfo.fAlarmTemperature;
                    System.out.println("人脸温度信息:" + sTemperatureInfo);
                }
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                    String time = df.format(new Date());// new Date()为获取当前系统时间
                    //人脸图片写文件
                    FileOutputStream small = new FileOutputStream("../pic/" + time + "small.jpg");
                    FileOutputStream big = new FileOutputStream("../pic/" + time + "big.jpg");
                    try {
                        small.write(strFaceSnapInfo.pBuffer1.getByteArray(0, strFaceSnapInfo.dwFacePicLen), 0, strFaceSnapInfo.dwFacePicLen);
                        small.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        big.write(strFaceSnapInfo.pBuffer2.getByteArray(0, strFaceSnapInfo.dwBackgroundPicLen), 0, strFaceSnapInfo.dwBackgroundPicLen);
                        big.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                break;
            case HCNetSDK.COMM_SNAP_MATCH_ALARM:    //人脸黑名单比对报警
                HCNetSDK.NET_VCA_FACESNAP_MATCH_ALARM strFaceSnapMatch = new HCNetSDK.NET_VCA_FACESNAP_MATCH_ALARM();
                strFaceSnapMatch.write();
                Pointer pFaceSnapMatch = strFaceSnapMatch.getPointer();
                pFaceSnapMatch.write(0, pAlarmInfo.getByteArray(0, strFaceSnapMatch.size()), 0, strFaceSnapMatch.size());
                strFaceSnapMatch.read();
                //比对结果，0-保留，1-比对成功，2-比对失败
                String sFaceSnapMatchInfo = "比对结果：" + strFaceSnapMatch.byContrastStatus + ",相似度：" + strFaceSnapMatch.fSimilarity;
                System.out.println(sFaceSnapMatchInfo);
                if (strFaceSnapMatch.struBlockListInfo.dwFDIDLen > 0) {
                    long offset1 = 0;
                    ByteBuffer buffers1 = strFaceSnapMatch.struBlockListInfo.pFDID.getByteBuffer(offset1, strFaceSnapMatch.struBlockListInfo.dwFDIDLen);
                    byte[] bytes1 = new byte[strFaceSnapMatch.struBlockListInfo.dwFDIDLen];
                    buffers1.get(bytes1);
                    System.out.println("人脸库ID:" + new String(bytes1));
                }
                if (strFaceSnapMatch.struBlockListInfo.dwPIDLen > 0) {
                    long offset2 = 0;
                    ByteBuffer buffers2 = strFaceSnapMatch.struBlockListInfo.pPID.getByteBuffer(offset2, strFaceSnapMatch.struBlockListInfo.dwPIDLen);
                    byte[] bytes2 = new byte[strFaceSnapMatch.struBlockListInfo.dwPIDLen];
                    buffers2.get(bytes2);
                    System.out.println("图片ID：" + new String(bytes2));
                }
                //抓拍库附加信息解析（解析人脸测温温度,人脸温度存放在附件信息的XML报文中，节点：  <currTemperature> ）
                SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
                String curTime2 = sf2.format(new Date());
                FileOutputStream AddtionData;
                String AddtionFile = "../pic" + new String(pAlarmer.sDeviceIP).trim() + curTime2 + "_FCAdditionInfo_" + ".xml";
                try {
                    FileOutputStream Data = new FileOutputStream(AddtionFile);
                    //将字节写入文件
                    ByteBuffer dataBuffer = strFaceSnapMatch.struBlockListInfo.struBlockListInfo.pFCAdditionInfoBuffer.getByteBuffer(0, strFaceSnapMatch.struBlockListInfo.struBlockListInfo.dwFCAdditionInfoLen);
                    byte[] dataByte = new byte[dwBufLen];
                    dataBuffer.rewind();
                    dataBuffer.get(dataByte);
                    Data.write(dataByte);
                    Data.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //人脸比对报警图片保存，图片格式二进制
                if ((strFaceSnapMatch.dwSnapPicLen > 0) && (strFaceSnapMatch.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + newName + "_pSnapPicBuffer" + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = strFaceSnapMatch.pSnapPicBuffer.getByteBuffer(offset, strFaceSnapMatch.dwSnapPicLen);
                        byte[] bytes = new byte[strFaceSnapMatch.dwSnapPicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if ((strFaceSnapMatch.struSnapInfo.dwSnapFacePicLen > 0) && (strFaceSnapMatch.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + newName + "_struSnapInfo_pBuffer1" + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = strFaceSnapMatch.struSnapInfo.pBuffer1.getByteBuffer(offset, strFaceSnapMatch.struSnapInfo.dwSnapFacePicLen);
                        byte[] bytes = new byte[strFaceSnapMatch.struSnapInfo.dwSnapFacePicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if ((strFaceSnapMatch.struBlockListInfo.dwBlockListPicLen > 0) && (strFaceSnapMatch.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + newName + "_fSimilarity_" + strFaceSnapMatch.fSimilarity + "_struBlackListInfo_pBuffer1" + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = strFaceSnapMatch.struBlockListInfo.pBuffer1.getByteBuffer(offset, strFaceSnapMatch.struBlockListInfo.dwBlockListPicLen);
                        byte[] bytes = new byte[strFaceSnapMatch.struBlockListInfo.dwBlockListPicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //人脸比对报警图片保存，图片格式URL格式
                if ((strFaceSnapMatch.dwSnapPicLen > 0) && (strFaceSnapMatch.byPicTransType == 1)) {
                    long offset = 0;
                    ByteBuffer buffers = strFaceSnapMatch.pSnapPicBuffer.getByteBuffer(offset, strFaceSnapMatch.dwSnapPicLen);
                    byte[] bytes = new byte[strFaceSnapMatch.dwSnapPicLen];
                    buffers.rewind();
                    buffers.get(bytes);
                    String SnapPicUrl = new String(bytes);
                    System.out.println("抓拍图URL：" + SnapPicUrl);
                }
                if ((strFaceSnapMatch.struSnapInfo.dwSnapFacePicLen > 0) && (strFaceSnapMatch.byPicTransType == 1)) {
                    long offset = 0;
                    ByteBuffer buffers = strFaceSnapMatch.struSnapInfo.pBuffer1.getByteBuffer(offset, strFaceSnapMatch.struSnapInfo.dwSnapFacePicLen);
                    byte[] bytes = new byte[strFaceSnapMatch.struSnapInfo.dwSnapFacePicLen];
                    buffers.rewind();
                    buffers.get(bytes);
                    String SnapPicUrl = new String(bytes);
                    System.out.println("抓拍人脸子图URL：" + SnapPicUrl);
                }
                if ((strFaceSnapMatch.struBlockListInfo.dwBlockListPicLen > 0) && (strFaceSnapMatch.byPicTransType == 1)) {

                    long offset = 0;
                    ByteBuffer buffers = strFaceSnapMatch.struBlockListInfo.pBuffer1.getByteBuffer(offset, strFaceSnapMatch.struBlockListInfo.dwBlockListPicLen);
                    byte[] bytes = new byte[strFaceSnapMatch.struBlockListInfo.dwBlockListPicLen];
                    buffers.rewind();
                    buffers.get(bytes);
                    String SnapPicUrl = new String(bytes);
                    System.out.println("人脸库人脸图的URL：" + SnapPicUrl);
                }
                break;
            //  客流量报警信息
            case HCNetSDK.COMM_ALARM_PDC:
                HCNetSDK.NET_DVR_PDC_ALRAM_INFO strPDCResult = new HCNetSDK.NET_DVR_PDC_ALRAM_INFO();
                strPDCResult.write();
                Pointer pPDCInfo = strPDCResult.getPointer();
                pPDCInfo.write(0, pAlarmInfo.getByteArray(0, strPDCResult.size()), 0, strPDCResult.size());
                strPDCResult.read();
                // byMode=0-实时统计结果(联合体中struStatFrame有效)，
                if (strPDCResult.byMode == 0) {
                    strPDCResult.uStatModeParam.setType(HCNetSDK.NET_DVR_STATFRAME.class);
                    String sAlarmPDC0Info = "实时客流量统计，进入人数：" + strPDCResult.dwEnterNum + "，离开人数：" + strPDCResult.dwLeaveNum + ", byMode:" + strPDCResult.byMode + ", dwRelativeTime:" + strPDCResult.uStatModeParam.struStatFrame.dwRelativeTime + ", dwAbsTime:" + strPDCResult.uStatModeParam.struStatFrame.dwAbsTime;
                }
                // byMode=1-周期统计结果(联合体中struStatTime有效)，
                if (strPDCResult.byMode == 1) {
                    strPDCResult.uStatModeParam.setType(HCNetSDK.NET_DVR_STATTIME.class);
                    String strtmStart = "" + String.format("%04d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwYear) + String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwMonth) + String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwDay) + String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwHour) + String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwMinute) + String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwSecond);
                    String strtmEnd = "" + String.format("%04d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwYear) + String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwMonth) + String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwDay) + String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwHour) + String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwMinute) + String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwSecond);
                    String sAlarmPDC1Info = "周期性客流量统计，进入人数：" + strPDCResult.dwEnterNum + "，离开人数：" + strPDCResult.dwLeaveNum + ", byMode:" + strPDCResult.byMode + ", tmStart:" + strtmStart + ",tmEnd :" + strtmEnd;
                }
                break;
            case HCNetSDK.COMM_ALARM_V30:  //移动侦测、视频丢失、遮挡、IO信号量等报警信息(V3.0以上版本支持的设备)
                HCNetSDK.NET_DVR_ALARMINFO_V30 struAlarmInfo = new HCNetSDK.NET_DVR_ALARMINFO_V30();
                struAlarmInfo.write();
                Pointer pAlarmInfo_V30 = struAlarmInfo.getPointer();
                pAlarmInfo_V30.write(0, pAlarmInfo.getByteArray(0, struAlarmInfo.size()), 0, struAlarmInfo.size());
                struAlarmInfo.read();
                System.out.println("报警类型：" + struAlarmInfo.dwAlarmType);  // 3-移动侦测
                break;
            case HCNetSDK.COMM_ALARM_V40: //移动侦测、视频丢失、遮挡、IO信号量等报警信息，报警数据为可变长
                HCNetSDK.NET_DVR_ALARMINFO_V40 struAlarmInfoV40 = new HCNetSDK.NET_DVR_ALARMINFO_V40();
                struAlarmInfoV40.write();
                Pointer pAlarmInfoV40 = struAlarmInfoV40.getPointer();
                pAlarmInfoV40.write(0, pAlarmInfo.getByteArray(0, struAlarmInfoV40.size()), 0, struAlarmInfoV40.size());
                struAlarmInfoV40.read();
                System.out.println("报警类型:" + struAlarmInfoV40.struAlarmFixedHeader.dwAlarmType); //3-移动侦测
                break;
            case HCNetSDK.COMM_THERMOMETRY_ALARM:  //温度报警信息
                HCNetSDK.NET_DVR_THERMOMETRY_ALARM struTemInfo = new HCNetSDK.NET_DVR_THERMOMETRY_ALARM();
                struTemInfo.write();
                Pointer pTemInfo = struTemInfo.getPointer();
                pTemInfo.write(0, pAlarmInfo.getByteArray(0, struTemInfo.size()), 0, struTemInfo.size());
                struTemInfo.read();
                String sThermAlarmInfo = "规则ID:" + struTemInfo.byRuleID + "预置点号：" + struTemInfo.wPresetNo + "报警等级：" + struTemInfo.byAlarmLevel + "报警类型：" + struTemInfo.byAlarmType + "当前温度：" + struTemInfo.fCurrTemperature;
                System.out.println(sThermAlarmInfo);
                //可见光图片保存
                if ((struTemInfo.dwPicLen > 0) && (struTemInfo.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + newName + "_" + struTemInfo.fCurrTemperature + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struTemInfo.pPicBuff.getByteBuffer(offset, struTemInfo.dwPicLen);
                        byte[] bytes = new byte[struTemInfo.dwPicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if ((struTemInfo.dwThermalPicLen > 0) && (struTemInfo.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + newName + "_" + "_ThermalPiC" + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struTemInfo.pThermalPicBuff.getByteBuffer(offset, struTemInfo.dwThermalPicLen);
                        byte[] bytes = new byte[struTemInfo.dwThermalPicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case HCNetSDK.COMM_THERMOMETRY_DIFF_ALARM: //温差检测报警
                HCNetSDK.NET_DVR_THERMOMETRY_DIFF_ALARM strThermDiffAlarm = new HCNetSDK.NET_DVR_THERMOMETRY_DIFF_ALARM();
                strThermDiffAlarm.write();
                Pointer pTemDiffInfo = strThermDiffAlarm.getPointer();
                pTemDiffInfo.write(0, pAlarmInfo.getByteArray(0, strThermDiffAlarm.size()), 0, strThermDiffAlarm.size());
                strThermDiffAlarm.read();
                String sThremDiffInfo = "通道号：" + strThermDiffAlarm.dwChannel + ",报警规则：" + strThermDiffAlarm.byAlarmRule + "，当前温差：" + strThermDiffAlarm.fCurTemperatureDiff;
                System.out.println(sThremDiffInfo);
                //可见光图片保存
                if ((strThermDiffAlarm.dwPicLen > 0) && (strThermDiffAlarm.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + newName + "_" + strThermDiffAlarm.fCurTemperatureDiff + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = strThermDiffAlarm.pPicBuff.getByteBuffer(offset, strThermDiffAlarm.dwPicLen);
                        byte[] bytes = new byte[strThermDiffAlarm.dwPicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //热成像图片保存
                if ((strThermDiffAlarm.dwThermalPicLen > 0) && (strThermDiffAlarm.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + newName + "_" + "_ThermalDiffPiC" + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = strThermDiffAlarm.pThermalPicBuff.getByteBuffer(offset, strThermDiffAlarm.dwThermalPicLen);
                        byte[] bytes = new byte[strThermDiffAlarm.dwThermalPicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case HCNetSDK.COMM_FIREDETECTION_ALARM://烟火检测
                HCNetSDK.NET_DVR_FIREDETECTION_ALARM struFireDecAlarm = new HCNetSDK.NET_DVR_FIREDETECTION_ALARM();
                struFireDecAlarm.write();
                Pointer pFireDecAlarm = struFireDecAlarm.getPointer();
                pFireDecAlarm.write(0, pAlarmInfo.getByteArray(0, struFireDecAlarm.size()), 0, struFireDecAlarm.size());
                struFireDecAlarm.read();
                String sFireDecAlarmInfo = "绝对时间：" + struFireDecAlarm.dwAbsTime + ",报警子类型：" + struFireDecAlarm.byAlarmSubType + ",火点最高温度 :" + struFireDecAlarm.wFireMaxTemperature + ",火点目标距离：" + struFireDecAlarm.wTargetDistance;
                System.out.println(sFireDecAlarmInfo);
                //可见光图片保存
                if ((struFireDecAlarm.dwVisiblePicLen > 0) && (struFireDecAlarm.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;

                    try {
                        String filename = "../pic/" + newName + "_FireDecAlarm" + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struFireDecAlarm.pVisiblePicBuf.getByteBuffer(offset, struFireDecAlarm.dwVisiblePicLen);
                        byte[] bytes = new byte[struFireDecAlarm.dwVisiblePicLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //热成像图片保存
                if ((struFireDecAlarm.dwPicDataLen > 0) && (struFireDecAlarm.byPicTransType == 0)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        String filename = "../pic/" + newName + "_" + "_ThermalFireAlarm" + ".jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struFireDecAlarm.pBuffer.getByteBuffer(offset, struFireDecAlarm.dwPicDataLen);
                        byte[] bytes = new byte[struFireDecAlarm.dwPicDataLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return analyzerData;
    }

}
