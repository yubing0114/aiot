/*
 * Copyright (c) 2019, Zhejiang uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     :
 * SdkModule Name :
 * Date Created: 2020-04-07 10:42
 * Creator     : s04180
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *
 *------------------------------------------------------------------------------
 */
package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.manager;

import com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.constant.LapiUriConstat;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * HttpConnectionThread
 *
 * @author s04180
 * @Description
 */
@Component
public class RequestDeviceTestThread implements Runnable {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RequestDeviceTestThread.class);

    //终端接口地址URI
    private String api = LapiUriConstat.PEOPLE_BASIC_INFO_URI;

    //终端设备的序列号 or  响应心跳的 deviceCode
    private String serialNo = "210235C4C53203016220";

    //请求的接口参数
    private String json = "";

//    @Autowired
//    private DeviceService deviceService;


    @Override
    public void run() {
//        List<Device> deviceList = deviceService.getAll();

        try {
            FullHttpRequest httpRequest = null;

            //POST
            // httpRequest =  RequestDeviceManager.createRequestDeviceInfo(api, json,HttpMethod.POST);

            //DELETE
            // httpRequest =RequestDeviceManager.createRequestDeviceInfo(api, json,HttpMethod.DELETE);

            //PUT
            // httpRequest =RequestDeviceManager.createRequestDeviceInfo(api, json,HttpMethod.PUT);

            //GET
            httpRequest = RequestDeviceManager.createRequestDeviceInfo(api, json, HttpMethod.GET);

            HttpKeepAliveManager.getResponseMapByKeepAliveConnection(serialNo, httpRequest);

//                Map<String, String> map = null;
//                for (Device device : deviceList) {
//                    map = HttpKeepAliveManager.getResponseMapByKeepAliveConnection(device.getSerialNum(), httpRequest);
//                    if (Objects.nonNull(map)) {
////                        LOGGER.info("Device:{} Response Info:\n{}", serialNo, JSON.toJSONString(map, SerializerFeature.PrettyFormat));
//                        LOGGER.warn(device.getSerialNum() + "请求正常 ... ... ");
//                    } else {
//                        LOGGER.warn(device.getSerialNum() + "请求超时 ... ... ");
//                    }
//                }
        } catch (Exception e) {
            LOGGER.error("{}", e);
        }
    }
}
