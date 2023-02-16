package com.yb.aiot.module.media.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Media默认服务器配置
 * <p>
 *
 * @author author
 * @date 2022/10/17 14:57
 */
@Component
@ConfigurationProperties(prefix = "media")
@Data
public class MediaConfig {

    @ApiModelProperty("服务器ip")
    private String ip;

    @ApiModelProperty("HTTP端口")
    private Integer httpPort;

    @ApiModelProperty("ZLM鉴权参数")
    private String secret;

    @ApiModelProperty("服务器唯一id，用于触发hook时区别是哪台服务器")
    private String serverId;

}
