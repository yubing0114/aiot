package com.yb.aiot.module.sdk.callback.hik;

import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Pointer;
import com.yb.aiot.module.sdk.netsdk.hik.HCNetSDK;
import com.yb.aiot.module.sdk.utils.CallbackUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 海康事件回调
 * <p>
 *
 * @author author
 * @date 2022/5/6 16:53
 */
@Service
public class FmsgCBV31 implements HCNetSDK.FMSGCallBack_V31 {

    private static FmsgCBV31 instance = null;

    private FmsgCBV31() {
    }

    public static FmsgCBV31 getInstance() {
        if (instance == null) {
            synchronized (FmsgCBV31.class) {
                if (instance == null) {
                    instance = new FmsgCBV31();
                }
            }
        }
        return instance;
    }

    /**
     * 报警信息回调函数
     *
     * @param lCommand
     * @param pAlarmer
     * @param pAlarmInfo
     * @param dwBufLen
     * @param pUser
     * @return boolean
     */
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {

        JSONObject fmsgData = FmsgData.AlarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
        boolean flag = false;
        if (fmsgData != null && fmsgData.getString("picture") != null) {
            switch (fmsgData.getString("flag")) {
                case "camera":
                    flag = CallbackUtil.isSendCbData(fmsgData);
                    break;
                case "entrance":
                    flag = true;
                    if (fmsgData.getString("cardNo") != null) {
//                        SdkUtil.sendAttendanceCbData(fmsgData);
                    }
                    break;
                case "parking":
                    flag = true;
                    break;
            }
            CallbackUtil.sendCbData(flag, fmsgData);
        }
        return true;
    }

}





