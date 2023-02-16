package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.manager;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 */
public class RequestDeviceManager {

    private static final Logger logger = LoggerFactory.getLogger(RequestDeviceManager.class);

    /**
     * 创建请求设备的http信息
     *
     * @param api        请求的uri
     * @param json       http请求的body结构体，不包含http header信息,为json字符串
     * @param httpMethod 请求的http方法 HttpMethod.POST, HttpMethod.PUT, HttpMethod.GET, HttpMethod.Delete
     * @return
     * @throws URISyntaxException
     */
    public static FullHttpRequest createRequestDeviceInfo(String api, String json, HttpMethod httpMethod) {
        FullHttpRequest request = null;
        URI url = null;
        try {
            url = new URI(api);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotBlank(json)) {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, httpMethod, url.toASCIIString(), Unpooled.wrappedBuffer(json.getBytes(CharsetUtil.UTF_8)));
            request.headers()
                    .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        } else {
            request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, httpMethod, url.toASCIIString());
        }
        request.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        return request;
    }

}
