package com.yb.aiot.utils;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

import java.net.ConnectException;
import java.util.HashMap;

/**
 * <p>
 * Http工具类
 * <p>
 *
 * @author author
 * @date 2022/10/21 13:57
 */
public class MyHttpUtil {

    private static final int timeout = 3000;

    /**
     * 发送get请求并接收响应数据
     *
     * @param url url
     * @return com.alibaba.fastjson.JSONObject
     */
    public static JSONObject get(String url) throws IORuntimeException {
        String result = HttpUtil.createGet(url).timeout(timeout).execute().body();
        return JSONObject.parseObject(result);
    }

    /**
     * 发送get请求并接收响应数据
     *
     * @param url     url
     * @param headers headers
     * @return com.alibaba.fastjson.JSONObject
     */
    public static JSONObject get(String url, HashMap<String, String> headers) throws IORuntimeException {
        String result = HttpUtil.createGet(url).addHeaders(headers).timeout(timeout).execute().body();
        return JSONObject.parseObject(result);
    }

    /**
     * 发送post请求并接收响应数据
     *
     * @param url        url
     * @param jsonString jsonString
     * @return com.alibaba.fastjson.JSONObject
     */
    public static JSONObject post(String url, String jsonString) throws IORuntimeException {
        String result = HttpUtil.createPost(url).body(jsonString).timeout(timeout).execute().body();
        return JSONObject.parseObject(result);
    }

    /**
     * 发送post请求并接收响应数据
     *
     * @param url        url
     * @param headers    headers
     * @param jsonString jsonString
     * @return com.alibaba.fastjson.JSONObject
     */
    public static JSONObject post(String url, HashMap<String, String> headers, String jsonString) throws IORuntimeException {
        String result = HttpUtil.createPost(url).addHeaders(headers).body(jsonString).timeout(timeout).execute().body();
        return JSONObject.parseObject(result);
    }

}


