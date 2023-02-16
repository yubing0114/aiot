package com.yb.aiot.module.sdk.netsdk.boan;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * boan平台对接工具类
 * <p>
 *
 * @author author
 * @date 2022/8/22 16:06
 */
public class BoanPlatform {

//    static JSONObject platformInfo = YmlUtil.getJSONObject("boan");
    static JSONObject platformInfo = null;

    public static JSONObject sendPost(String api, JSONObject param) {
        String url = String.format("http://%s:%s/parkapi/%s", platformInfo.getString("ip"), platformInfo.getString("port"), api);
        System.out.println(url);
//        return IPUtil.post(url, param);
        return null;
    }

    /**
     * 远程开闸
     *
     * @param param
     * @return com.alibaba.fastjson.JSONObject
     */
    public JSONObject openDoor(JSONObject param) {
        JSONObject params = new JSONObject();
        // 操作员编号
        params.put("operate_code", "");
        // 操作员名称, 否(当操作员为外部人员时必传)
        params.put("operate_name", "");
        // 设备ip,与车道 ID 二选一
        params.put("deviceip", "");
        // 命令 Integer 0=关闸 1=开闸
        params.put("command", "");
        // 车场编号 Long
        params.put("parkid", "");
        // 签名
        params.put("sign", BoanSign.sign(params, "test"));
        return sendPost("openDoor", param);
    }

    /**
     * 请求图片
     *
     * @param param
     * @return com.alibaba.fastjson.JSONObject
     */
    public JSONObject imageSync(JSONObject param) {
        JSONObject params = new JSONObject();
        // 入场或者出场记录的ID
        params.put("order_id", "");
        // 进场时间 Long
        params.put("intime", "");
        // 车场编号 Long
        params.put("parkid", "");
        // 签名 String
        params.put("sign", BoanSign.sign(params, "test"));
        return sendPost("image_sync", param);
    }

    /**
     * 无牌车入场
     *
     * @param param
     * @return com.alibaba.fastjson.JSONObject
     */
    public static JSONObject novehiclein(JSONObject param) {
        JSONObject params = new JSONObject();
        // 车主身份唯一标识（可为手机号、公众号的 openid）
        params.put("uid", "1");
        // 进出场的车道 ID Int
        params.put("channel_id", "1");
        // 车场 ID String
        params.put("parkid", "1");
        // 签名 String
        params.put("sign", BoanSign.sign(params, "test"));
        System.out.println(params);
        return sendPost("novehiclein", param);
    }

}
