package com.yb.aiot.module.sdk.callback.dahua;

import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Pointer;
import com.yb.aiot.module.sdk.netsdk.dahua.lib.NetSDKLib;
import com.yb.aiot.module.sdk.utils.CallbackUtil;

/**
 * <p>
 * 大华事件回调
 * <p>
 *
 * @author: author
 * @date: 2022/4/25 17:13
 */
public class AnalyzerDataCB implements NetSDKLib.fAnalyzerDataCallBack {

    private static AnalyzerDataCB instance = null;

    private AnalyzerDataCB() {
    }

    public static AnalyzerDataCB getInstance() {
        if (instance == null) {
            synchronized (AnalyzerDataCB.class) {
                if (instance == null) {
                    instance = new AnalyzerDataCB();
                }
            }
        }
        return instance;
    }

    // 回调
    public int invoke(NetSDKLib.LLong lAnalyzerHandle, int dwAlarmType, Pointer pAlarmInfo, Pointer pBuffer, int dwBufSize, Pointer dwUser, int nSequence, Pointer reserved) {
        if (lAnalyzerHandle == null || lAnalyzerHandle.longValue() == 0) {
            return -1;
        }
        JSONObject analyzerData = AnalyzerData.analyze(lAnalyzerHandle, dwAlarmType, pAlarmInfo, pBuffer, dwBufSize);
        boolean flag = false;
        if (analyzerData != null) {
            switch (analyzerData.getString("flag")) {
                case "camera":
                    flag = CallbackUtil.isSendCbData(analyzerData);
                    break;
                case "entrance":
                    flag = true;
                    break;
                case "parking":
                    flag = true;
                    break;
            }
            CallbackUtil.sendCbData(flag, analyzerData);
        }
        return 1;
    }

}
