package com.yb.aiot.module.sdk.module.hik;

import cn.hutool.core.net.NetUtil;
import cn.hutool.log.StaticLog;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.yb.aiot.module.common.utils.MyFileUtil;
import com.yb.aiot.module.sdk.callback.hik.FmsgCBV31;
import com.yb.aiot.module.sdk.netsdk.hik.HCNetSDK;
import com.yb.aiot.module.sdk.utils.SdkUtil;
import com.yb.aiot.module.sdk.dto.DeviceDto;
import com.yb.aiot.module.sdk.module.SdkModule;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 海康SDk工具类
 * <p>
 *
 * @author author
 * @date 2022/5/5 10:38
 */
public class HikModule implements SdkModule {

    public static HCNetSDK hCNetSDK;

    public static Map<String, Integer> loginMap = new HashMap<>();

    public static Map<String, Integer> alarmMap = new HashMap<>();

    /**
     * 动态库加载
     *
     * @param
     * @return void
     */
    public static boolean createSDKInstance() {
        String strDllPath = SdkUtil.getSdkPath("hik", "libhcnetsdk.so");
        OsInfo osInfo = SystemUtil.getOsInfo();
        if (osInfo.isWindows()) {
            strDllPath = SdkUtil.getSdkPath("hik", "HCNetSDK.dll");
        }
        hCNetSDK = Native.load(strDllPath, HCNetSDK.class);
        if (hCNetSDK == null) {
            System.out.println("hik createSDKInstance fail");
            return false;
        }
        if (osInfo.isLinux()) {
            //linux系统建议调用以下接口加载组件库
            HCNetSDK.BYTE_ARRAY ptrByteArray1 = new HCNetSDK.BYTE_ARRAY(256);
            HCNetSDK.BYTE_ARRAY ptrByteArray2 = new HCNetSDK.BYTE_ARRAY(256);
            //这里是库的绝对路径，请根据实际情况修改，注意改路径必须有访问权限
            String strPath1 = SdkUtil.getSdkPath("hik", "libcrypto.so.1.1");
            String strPath2 = SdkUtil.getSdkPath("hik", "libssl.so.1.1");

            System.arraycopy(strPath1.getBytes(), 0, ptrByteArray1.byValue, 0, strPath1.length());
            ptrByteArray1.write();
            hCNetSDK.NET_DVR_SetSDKInitCfg(3, ptrByteArray1.getPointer());

            System.arraycopy(strPath2.getBytes(), 0, ptrByteArray2.byValue, 0, strPath2.length());
            ptrByteArray2.write();
            hCNetSDK.NET_DVR_SetSDKInitCfg(4, ptrByteArray2.getPointer());

            String strPathCom = SdkUtil.getSdkPath("hik", "");
            HCNetSDK.NET_DVR_LOCAL_SDK_PATH struComPath = new HCNetSDK.NET_DVR_LOCAL_SDK_PATH();
            System.arraycopy(strPathCom.getBytes(), 0, struComPath.sPath, 0, strPathCom.length());
            struComPath.write();
            hCNetSDK.NET_DVR_SetSDKInitCfg(2, struComPath.getPointer());
        }
        System.out.println("hik createSDKInstance success");
        // 初始化
        hCNetSDK.NET_DVR_Init();
        // 设置报警回调函数
        if (hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(FmsgCBV31.getInstance(), null)) {
            System.out.println("hik succeed to set up the callback function!");
        } else {
            System.out.println("hik failed to set up the callback function!");
        }
        // 本地IP地址置为null时自动获取本地IP
        short port = (short) NetUtil.getUsableLocalPort();
        int lListenHandle = hCNetSDK.NET_DVR_StartListen_V30(null, port, FmsgCBV31.getInstance(), null);
        if (lListenHandle != -1) {
            System.out.println("hik start listening successfully!");
        } else {
            System.out.println("hik 开始监听失败!");
        }
        /**
         * 设备上传的报警信息是COMM_VCA_ALARM(0x4993)类型，在SDK初始化之后增加调用
         * NET_DVR_SetSDKLocalCfg(enumType为NET_DVR_LOCAL_CFG_TYPE_GENERAL)
         * 设置通用参数NET_DVR_LOCAL_GENERAL_CFG的byAlarmJsonPictureSeparate为1，
         * 报警信息结构体为NET_DVR_ALARM_ISAPI_INFO（与设备无关，SDK封装的数据结构），更便于解析。
         */
        HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG struNET_DVR_LOCAL_GENERAL_CFG = new HCNetSDK.NET_DVR_LOCAL_GENERAL_CFG();
        // 设置JSON透传报警数据和图片分离
        struNET_DVR_LOCAL_GENERAL_CFG.byAlarmJsonPictureSeparate = 1;
        struNET_DVR_LOCAL_GENERAL_CFG.write();
        Pointer pStrNET_DVR_LOCAL_GENERAL_CFG = struNET_DVR_LOCAL_GENERAL_CFG.getPointer();
        hCNetSDK.NET_DVR_SetSDKLocalCfg(17, pStrNET_DVR_LOCAL_GENERAL_CFG);
        return true;
    }

    @Override
    public String getLastError() {
        return ",错误码：" + hCNetSDK.NET_DVR_GetLastError();
    }

    /**
     * 登陆
     *
     * @param device
     * @return void
     */
    public Object login(DeviceDto device) {
        if (loginMap.get(device.getIp()) != null) {
            return loginMap.get(device.getIp());
        }
        HCNetSDK.NET_DVR_DEVICEINFO_V30 lpDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        int lUserID = hCNetSDK.NET_DVR_Login_V30(device.getIp(), (short) device.getPort(), device.getUsername(), device.getPassword(), lpDeviceInfo);
        if (lUserID != -1) {
            System.out.println(device.getIp() + " 登陆成功");
            loginMap.put(device.getIp(), lUserID);
        } else {
            System.out.println(device.getIp() + " 登陆失败");
        }
        return lUserID;
    }

    @Override
    public boolean loginCheck(DeviceDto deviceDto) {
        HCNetSDK.NET_DVR_DEVICEINFO_V30 lpDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        int lUserID = hCNetSDK.NET_DVR_Login_V30(deviceDto.getIp(), (short) deviceDto.getPort(), deviceDto.getUsername(), deviceDto.getPassword(), lpDeviceInfo);
        if (lUserID != -1) {
            hCNetSDK.NET_DVR_Logout_V30(lUserID);
            System.out.println(deviceDto.getIp() + " 登陆成功");
            return true;
        }
        System.out.println(deviceDto.getIp() + " 登陆失败");
        return false;
    }

    /**
     * 布防
     *
     * @param device
     * @return boolean
     */
    public boolean openAlarm(DeviceDto device) {
        if (alarmMap.get(device.getIp()) != null) {
            closeAlarm(device.getIp());
        }
        int lUserID = (int) login(device);
        // 报警布防参数设置
        HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
        m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
        // 布防优先级，0-一等级（高），1-二等级（中），2-三等级（低）
        m_strAlarmInfo.byLevel = 0;
        // 智能交通报警信息上传类型：0- 老报警信息（NET_DVR_PLATE_RESULT），1- 新报警信息(NET_ITS_PLATE_RESULT)
        m_strAlarmInfo.byAlarmInfoType = 1;
        // 布防类型：0-客户端布防，1-实时布防
        m_strAlarmInfo.byDeployType = 1;
        m_strAlarmInfo.write();
        int lAlarmHandle = hCNetSDK.NET_DVR_SetupAlarmChan_V41(lUserID, m_strAlarmInfo);
        if (lAlarmHandle == -1) {

            System.out.println(String.format("%s 布防失败,%s", device.getIp(), hCNetSDK.NET_DVR_GetLastError()));
            return false;
        } else {
            System.out.println(String.format("%s 布防成功", device.getIp()));
            alarmMap.put(device.getIp(), lAlarmHandle);
            return true;
        }
    }

    /**
     * 撤防
     *
     * @param deviceIp
     * @return boolean
     */
    public boolean closeAlarm(String deviceIp) {
        if (alarmMap.get(deviceIp) == null) return false;
        if (hCNetSDK.NET_DVR_CloseAlarmChan_V30(alarmMap.get(deviceIp))) {
            System.out.println(deviceIp + "撤防成功! lAlarmHandle:" + alarmMap.get(deviceIp));
            alarmMap.remove(deviceIp);
            return true;
        }
        return false;
    }

    /**
     * 注销
     *
     * @param ip
     * @return void
     */
    public void logout(String ip) {
        if (hCNetSDK.NET_DVR_Logout_V30(loginMap.get(ip))) {
            System.out.println(ip + "注销成功! UserID:" + loginMap.get(ip));
            StaticLog.info("This is static {} log.", ip + "注销成功! UserID:" + loginMap.get(ip));
            loginMap.remove(ip);
        }
    }

    /**
     * 云台控制
     *
     * @param deviceDto
     * @return boolean
     */
    public boolean ptzControl(DeviceDto deviceDto) {
        int lUserID = (int) login(deviceDto);
        if (lUserID == -1) {
            return false;
        }
        int command = 0;
        switch (deviceDto.getPtzCommand()) {
            case 0:
                command = 21;
                break;
            case 1:
                command = 22;
                break;
            case 2:
                command = 23;
                break;
            case 3:
                command = 24;
                break;
            case 4:
                command = 11;
                break;
            case 5:
                command = 12;
                break;
        }
        return hCNetSDK.NET_DVR_PTZControl_Other(lUserID, 1, command, deviceDto.getStartOrStop());
    }

    /**
     * 远程控门
     *
     * @param deviceDto
     * @return boolean
     */
    public boolean controlDoor(DeviceDto deviceDto) {
        int lUserID = (int) login(deviceDto);
        if (lUserID != -1) {
            return hCNetSDK.NET_DVR_ControlGateway(lUserID, 1, deviceDto.getControlCommand());
        }
        return false;
    }

    /**
     * 道闸控制
     *
     * @param deviceDto
     * @return com.yslz.aiot.module.device.Result
     */
    public boolean controlGate(DeviceDto deviceDto) {
        int lUserID = (int) login(deviceDto);
        if (lUserID != -1) {
            HCNetSDK.NET_DVR_BARRIERGATE_CFG cfg = new HCNetSDK.NET_DVR_BARRIERGATE_CFG();
            // 通道号
            cfg.dwChannel = 1;
            // 结构体大小
            cfg.dwSize = cfg.size();
            // 道闸号：0-表示无效值(设备需要做有效值判断)，1-道闸1
            cfg.byLaneNo = 1;
            // 控制参数：0-关闭道闸，1-开启道闸，2-停止道闸，3-锁定道闸
            cfg.byBarrierGateCtrl = (byte) deviceDto.getControlCommand();
            //保留，置为0
            cfg.byRes[0] = 0;
            //起竿操作
            Pointer name = cfg.getPointer();
            cfg.write();
            return hCNetSDK.NET_DVR_RemoteControl(lUserID, 3128, name, cfg.size());
        }
        return false;
    }

    /**
     * 设备重启
     *
     * @param deviceDto
     * @return boolean
     */
    public boolean reboot(DeviceDto deviceDto) {
        boolean flag = false;
        int lUserID = (int) login(deviceDto);
        if (lUserID != -1) {
            flag = hCNetSDK.NET_DVR_RebootDVR(lUserID);
            System.out.println(hCNetSDK.NET_DVR_GetLastError());
            hCNetSDK.NET_DVR_Logout(lUserID);
        }
        if (flag) {
            System.out.println(deviceDto.getIp() + "重启成功");
        } else {
            System.out.println(deviceDto.getIp() + "重启失败");
        }
        return flag;
    }

    public String capture(DeviceDto deviceDto) {
        HCNetSDK.NET_DVR_DEVICEINFO_V30 lpDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        int lUserID = hCNetSDK.NET_DVR_Login_V30(deviceDto.getIp(), (short) deviceDto.getPort(), deviceDto.getUsername(), deviceDto.getPassword(), lpDeviceInfo);
        if (lUserID == -1) {
            return null;
        }
        // 拍照
        HCNetSDK.NET_DVR_JPEGPARA strJpeg = new HCNetSDK.NET_DVR_JPEGPARA();
        strJpeg.wPicQuality = 0; //图像参数
        strJpeg.wPicSize = 5;
        // 单帧数据捕获图片
        byte[] filePath = MyFileUtil.getAbsolutePath(MyFileUtil.makeFileName("capture")).getBytes();
        boolean b = hCNetSDK.NET_DVR_CaptureJPEGPicture(lUserID, lpDeviceInfo.byStartChan, strJpeg, filePath);
        if (b) {
            System.out.println("抓拍成功");
        } else {
            System.out.println("抓拍失败!" + " err: " + hCNetSDK.NET_DVR_GetLastError());
        }
        hCNetSDK.NET_DVR_Logout(lUserID);
        return null;
    }

}
