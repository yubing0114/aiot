package com.yb.aiot.module.sdk.module.dahua;

import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.yb.aiot.module.common.utils.MyFileUtil;
import com.yb.aiot.module.sdk.callback.dahua.AnalyzerDataCB;
import com.yb.aiot.module.sdk.netsdk.dahua.common.Res;
import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;
import com.yb.aiot.module.sdk.netsdk.dahua.lib.ToolKits;
import com.yb.aiot.module.sdk.utils.SdkUtil;
import com.yb.aiot.module.sdk.dto.DeviceDto;
import com.yb.aiot.module.sdk.module.SdkModule;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * <p>
 * 大华SDk工具类
 * <p>
 *
 * @author author
 * @date 2022/5/5 10:38
 */
public class DhModule implements SdkModule {

    public static NetSDKLib netsdk;

    public static NetSDKLib configsdk;

    // 设备信息
    public static NetSDKLib.NET_DEVICEINFO_Ex m_stDeviceInfo = new NetSDKLib.NET_DEVICEINFO_Ex();

    private static final AnalyzerDataCB analyzerCallback = AnalyzerDataCB.getInstance();

    private static final Map<String, NetSDKLib.LLong> loginMap = new HashMap<>();

    public static Map<NetSDKLib.LLong, String> callbackMap = new HashMap<>();

    public static Map<String, NetSDKLib.LLong> realLoadMap = new HashMap<>();

    public static boolean createSDKInstance() {
        if (SystemUtil.getOsInfo().isWindows()) {
            netsdk = Native.load(SdkUtil.getSdkPath("dahua", "dhnetsdk.dll"), NetSDKLib.class);
            configsdk = Native.load(SdkUtil.getSdkPath("dahua", "dhconfigsdk.dll"), NetSDKLib.class);
        } else {
            netsdk = Native.load(SdkUtil.getSdkPath("dahua", "libdhnetsdk.so"), NetSDKLib.class);
            configsdk = Native.load(SdkUtil.getSdkPath("dahua", "libdhconfigsdk.so"), NetSDKLib.class);
        }
        NetSDKLib.fDisConnect disConnect = new NetSDKLib.fDisConnect() {
            @Override
            public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
                System.out.println("断线回调");
            }
        };
        if (!netsdk.CLIENT_Init(disConnect, null)) {
            System.out.println("dahua createSDKInstance fail");
            return false;
        }
        System.out.println("dahua createSDKInstance success");

        // 设置断线重连回调接口，设置过断线重连成功回调函数后，当设备出现断线情况，SDK内部会自动进行重连操作
        // 此操作为可选操作，但建议用户进行设置
        NetSDKLib.fHaveReConnect haveReConnect = new NetSDKLib.fHaveReConnect() {
            @Override
            public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
                System.out.println("网络连接恢复回调");
            }
        };
        netsdk.CLIENT_SetAutoReconnect(haveReConnect, null);
        // 设置登录超时时间和尝试次数，可选
        // 登录请求响应超时时间设置为5S
        int waitTime = 5000;
        // 登录时尝试建立链接2次
        int tryTimes = 2;
        netsdk.CLIENT_SetConnectTime(waitTime, tryTimes);
        // 设置更多网络参数，NET_PARAM的nWaittime，nConnectTryNum成员与CLIENT_SetConnectTime
        // 接口设置的登录设备超时时间和尝试次数意义相同,可选
        NetSDKLib.NET_PARAM netParam = new NetSDKLib.NET_PARAM();
        // 登录时尝试建立链接的超时时间
        netParam.nConnectTime = 10000;
        // 设置子连接的超时时间
        netParam.nGetConnInfoTime = 3000;
        // 获取设备信息超时时间，为0默认1000ms
        netParam.nGetDevInfoTime = 3000;
        netsdk.CLIENT_SetNetworkParam(netParam);
        return true;
    }

    @Override
    public String getLastError() {
        return null;
    }

    public Object login(DeviceDto device) {
        if (loginMap.get(device.getIp()) != null) {
            return loginMap.get(device.getIp());
        }
        NetSDKLib.LLong loginHandle = ClientLogin(device);
        if (loginHandle.longValue() != 0) {
            loginMap.put(device.getIp(), loginHandle);
        }
        return loginHandle;
    }

    @Override
    public boolean loginCheck(DeviceDto deviceDto) {
        NetSDKLib.LLong loginHandle = ClientLogin(deviceDto);
        if (loginHandle.longValue() != 0) {
            netsdk.CLIENT_Logout(loginHandle);
            return true;
        }
        return false;
    }

    public NetSDKLib.LLong ClientLogin(DeviceDto deviceDto) {
        // 入参
        NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY pstInParam = new NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY();
        pstInParam.nPort = deviceDto.getPort();
        pstInParam.szIP = deviceDto.getIp().getBytes();
        pstInParam.szPassword = deviceDto.getPassword().getBytes();
        pstInParam.szUserName = deviceDto.getUsername().getBytes();
        // 出参
        NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY pstOutParam = new NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY();
        pstOutParam.stuDeviceInfo = m_stDeviceInfo;
        NetSDKLib.LLong loginHandle = netsdk.CLIENT_LoginWithHighLevelSecurity(pstInParam, pstOutParam);
        if (loginHandle.longValue() != 0) {
            System.out.println(deviceDto.getIp() + " 登陆成功");
            return loginHandle;
        }
        System.out.println(deviceDto.getIp() + " 登陆失败");
        return loginHandle;
    }

    /**
     * 订阅实时上传智能分析数据
     *
     * @param device
     * @return boolean
     */
    public boolean openAlarm(DeviceDto device) {
        if (realLoadMap.get(device.getIp()) != null) {
            closeAlarm(device.getIp());
        }
        boolean flag = false;
        NetSDKLib.LLong loginHandle = (NetSDKLib.LLong) login(device);
        if (loginHandle.longValue() != 0) {
            NetSDKLib.LLong lAnalyzerHandle = netsdk.CLIENT_RealLoadPictureEx(loginHandle, 0, NetSDKLib.EVENT_IVS_ALL, 1, analyzerCallback, null, null);
            if (lAnalyzerHandle.longValue() != 0) {
                System.out.println(String.format("%s 布防成功", device.getIp()));
                realLoadMap.put(device.getIp(), lAnalyzerHandle);
                callbackMap.put(lAnalyzerHandle, device.getIp());
                flag = true;
            } else {
                System.err.printf(String.format("%s logout失败,%s", device.getIp(), "ToolKits.getErrorCodePrint()"));
            }
        }
        return flag;
    }

    public boolean closeAlarm(String ip) {
        boolean flag = false;
        NetSDKLib.LLong lAnalyzerHandle = realLoadMap.get(ip);
        if (lAnalyzerHandle != null) {
            flag = netsdk.CLIENT_StopLoadPic(lAnalyzerHandle);
            if (flag) {
                System.out.println("撤防 Success [ " + ip + " ]");
                realLoadMap.remove(ip);
                callbackMap.remove(lAnalyzerHandle);
            } else {
                System.err.printf("撤防 Failed[%s]%s", ip, "ToolKits.getErrorCodePrint()");
            }
        }
        return flag;
    }

    public boolean ptzControl(DeviceDto deviceDto) {
        NetSDKLib.LLong loginHandle = (NetSDKLib.LLong) login(deviceDto);
        boolean flag = false;
        if (loginHandle.longValue() == 0) {
            flag = netsdk.CLIENT_DHPTZControlEx(loginHandle, 0, deviceDto.getPtzCommand(), 0, 0, 0, deviceDto.getStartOrStop());
        }
        return flag;
    }

    /**
     * 远程控门
     *
     * @param deviceDto
     * @return boolean
     */
    public boolean controlDoor(DeviceDto deviceDto) {
        NetSDKLib.LLong loginHandle = (NetSDKLib.LLong) login(deviceDto);
        if (loginHandle.longValue() != 0) {
            // 关门
            if (deviceDto.getControlCommand() == 0) {
                final NetSDKLib.NET_CTRL_ACCESS_CLOSE close = new NetSDKLib.NET_CTRL_ACCESS_CLOSE();
                close.nChannelID = 0; // 对应的门编号 - 如何开全部的门
                close.write();
                boolean result = netsdk.CLIENT_ControlDeviceEx(loginHandle, NetSDKLib.CtrlType.CTRLTYPE_CTRL_ACCESS_CLOSE, close.getPointer(), null, 5000);
                close.read();
                return result;
            }
            NetSDKLib.NET_CTRL_ACCESS_OPEN openInfo = new NetSDKLib.NET_CTRL_ACCESS_OPEN();
            openInfo.nChannelID = 0;
            openInfo.emOpenDoorType = NetSDKLib.EM_OPEN_DOOR_TYPE.EM_OPEN_DOOR_TYPE_REMOTE;
            Pointer pointer = new Memory(openInfo.size());
            ToolKits.SetStructDataToPointer(openInfo, pointer, 0);
            return netsdk.CLIENT_ControlDeviceEx(loginHandle, NetSDKLib.CtrlType.CTRLTYPE_CTRL_ACCESS_OPEN, pointer, null, 10000);
        }
        return false;
    }

    @Override
    public boolean controlGate(DeviceDto deviceDto) {
        return false;
    }

    public boolean shutdown(DeviceDto deviceDto) {
        NetSDKLib.LLong loginHandle = (NetSDKLib.LLong) login(deviceDto);
        if (loginHandle.longValue() != 0) {
            int emType = NetSDKLib.CtrlType.CTRLTYPE_CTRL_SHUTDOWN;
            boolean flag = netsdk.CLIENT_ControlDevice(loginHandle, emType, null, 3000);
            System.out.println(flag);
            return flag;
        }
        return false;
    }

    public boolean reboot(DeviceDto deviceDto) {
        NetSDKLib.LLong loginHandle = (NetSDKLib.LLong) login(deviceDto);
        if (loginHandle.longValue() != 0) {
            int emType = NetSDKLib.CtrlType.CTRLTYPE_CTRL_REBOOT;
            return netsdk.CLIENT_ControlDevice(loginHandle, emType, null, 3000);
        }
        return false;
    }

    /**
     * 抓图
     *
     * @param deviceDto
     * @return java.lang.String
     */
    public String capture(DeviceDto deviceDto) {
        NetSDKLib.LLong loginHandle = (NetSDKLib.LLong) login(deviceDto);
        if (loginHandle.longValue() != 0) {
            String picName = MyFileUtil.makeFileName("capture");
            NetSDKLib.fSnapRev fSnapRev = new NetSDKLib.fSnapRev() {
                @Override
                public void invoke(NetSDKLib.LLong lLoginID, Pointer pBuf, int RevLen, int EncodeType, int CmdSerial, Pointer dwUser) {
                    if (pBuf != null && RevLen > 0) {
                        byte[] buf = pBuf.getByteArray(0, RevLen);
                        ByteArrayInputStream byteArrInput = new ByteArrayInputStream(buf);
                        try {
                            BufferedImage bufferedImage = ImageIO.read(byteArrInput);
                            if (bufferedImage != null) {
                                ImageIO.write(bufferedImage, "jpg", new File(MyFileUtil.getAbsolutePath(picName)));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            // 抓图回调函数
            netsdk.CLIENT_SetSnapRevCallBack(fSnapRev, null);
            // send caputre picture command to device
            NetSDKLib.SNAP_PARAMS stuSnapParams = new NetSDKLib.SNAP_PARAMS();
            // channel
            stuSnapParams.Channel = 0;
            // capture picture mode
            stuSnapParams.mode = 0;
            // picture quality
            stuSnapParams.Quality = 3;
            // timer capture picture time interval
            stuSnapParams.InterSnap = 0;
            // request serial
            stuSnapParams.CmdSerial = 0;
            IntByReference reserved = new IntByReference(0);
            boolean flag = netsdk.CLIENT_SnapPictureEx(loginHandle, stuSnapParams, reserved);
            if (flag) {
                return MyFileUtil.getDownFileUrl(picName);
            } else {
                return null;
            }
        }
        return null;
    }

    public List<JSONObject> findCard(DeviceDto deviceDto) {
        NetSDKLib.LLong loginHandle = (NetSDKLib.LLong) login(deviceDto);
        if (loginHandle.longValue() == 0) {
            return null;
        }
        NetSDKLib.FIND_RECORD_ACCESSCTLCARD_CONDITION findCondition = new NetSDKLib.FIND_RECORD_ACCESSCTLCARD_CONDITION();
        // CLIENT_FindRecord 接口入参
        NetSDKLib.NET_IN_FIND_RECORD_PARAM stIn = new NetSDKLib.NET_IN_FIND_RECORD_PARAM();
        stIn.emType = NetSDKLib.EM_NET_RECORD_TYPE.NET_RECORD_ACCESSCTLCARD;
        // CLIENT_FindRecord 接口出参
        NetSDKLib.NET_OUT_FIND_RECORD_PARAM stOut = new NetSDKLib.NET_OUT_FIND_RECORD_PARAM();
        findCondition.write();
        if (!netsdk.CLIENT_FindRecord(loginHandle, stIn, stOut, 5000)) {
            System.err.println("没查到卡信息!");
        }
        findCondition.read();
        int nFindCount = 100;
        // 用于申请内存
        NetSDKLib.NET_RECORDSET_ACCESS_CTL_CARD[] pstRecord = new NetSDKLib.NET_RECORDSET_ACCESS_CTL_CARD[nFindCount];
        for (int i = 0; i < nFindCount; i++) {
            pstRecord[i] = new NetSDKLib.NET_RECORDSET_ACCESS_CTL_CARD();
        }
        // CLIENT_FindNextRecord 接口入参
        NetSDKLib.NET_IN_FIND_NEXT_RECORD_PARAM stNextIn = new NetSDKLib.NET_IN_FIND_NEXT_RECORD_PARAM();
        stNextIn.lFindeHandle = stOut.lFindeHandle;
        stNextIn.nFileCount = nFindCount;  //想查询的记录条数
        // CLIENT_FindNextRecord 接口出参
        NetSDKLib.NET_OUT_FIND_NEXT_RECORD_PARAM stNextOut = new NetSDKLib.NET_OUT_FIND_NEXT_RECORD_PARAM();
        stNextOut.nMaxRecordNum = nFindCount;
        // 申请内存
        stNextOut.pRecordList = new Memory((long) pstRecord[0].dwSize * nFindCount);
        stNextOut.pRecordList.clear((long) pstRecord[0].dwSize * nFindCount);
        // 将数组内存拷贝给指针
        ToolKits.SetStructArrToPointerData(pstRecord, stNextOut.pRecordList);
        if (netsdk.CLIENT_FindNextRecord(stNextIn, stNextOut, 5000)) {
            if (stNextOut.nRetRecordNum == 0) {
                return null;
            }
            List<JSONObject> result = new LinkedList<>();
            JSONObject cardInfo = new JSONObject(new LinkedHashMap<>());
            // 获取卡信息
            ToolKits.GetPointerDataToStructArr(stNextOut.pRecordList, pstRecord);
            // 获取有用的信息
            NetSDKLib.NET_RECORDSET_ACCESS_CTL_CARD[] pstRecordEx = new NetSDKLib.NET_RECORDSET_ACCESS_CTL_CARD[stNextOut.nRetRecordNum];
            for (int i = 0; i < stNextOut.nRetRecordNum; i++) {
                pstRecordEx[i] = new NetSDKLib.NET_RECORDSET_ACCESS_CTL_CARD();
                pstRecordEx[i] = pstRecord[i];
                // 卡号
                cardInfo.put("szCardNo", new String(pstRecord[i].szCardNo).trim());
                // 卡名
                try {
                    cardInfo.put("szCardName", new String(pstRecord[i].szCardName, "GBK").trim());
                } catch (UnsupportedEncodingException e) {
                    cardInfo.put("szCardName", "");
                }
                // 记录集编号
                cardInfo.put("nRecNo", pstRecord[i].nRecNo);
                // 用户ID
                cardInfo.put("szUserID", new String(pstRecord[i].szUserID).trim());
                // 卡密码
                cardInfo.put("szPsw", new String(pstRecord[i].szPsw).trim());
                // 卡状态
                cardInfo.put("emStatus", Res.string().getCardStatus(pstRecord[i].emStatus));
                // 卡类型
                cardInfo.put("emType", Res.string().getCardType(pstRecord[i].emType));
                // 使用次数
                cardInfo.put("nUserTime", String.valueOf(pstRecord[i].nUserTime).trim());
                // 是否首卡, 1-true, 0-false
                cardInfo.put("bFirstEnter", pstRecord[i].bFirstEnter == 1 ? Res.string().getFirstEnter() : Res.string().getNoFirstEnter());
                // 是否有效, 1-true, 0-false
                cardInfo.put("bIsValid", pstRecord[i].bIsValid == 1 ? Res.string().getValid() : Res.string().getInValid());
                // 有效开始时间
                cardInfo.put("stuValidStartTime", pstRecord[i].stuValidStartTime.toStringTimeEx());
                // 有效结束时间
                cardInfo.put("stuValidEndTime", pstRecord[i].stuValidEndTime.toStringTimeEx());
                result.add(cardInfo);
            }
            System.out.println(result);
            return result;
        }
        return null;
    }

}
