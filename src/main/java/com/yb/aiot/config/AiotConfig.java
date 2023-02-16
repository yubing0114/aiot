package com.yb.aiot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * aiot配置
 * <p>
 *
 * @author author
 * @date 2022/10/17 14:57
 */
@Data
@Component
@ConfigurationProperties(prefix = "aiot")
public class AiotConfig {

    // 场所类型信息获取路径
    private String placeType;

    // 场所信息获取路径
    private String place;

    // 场所房间信息获取路径
    private String placeRoom;

}
