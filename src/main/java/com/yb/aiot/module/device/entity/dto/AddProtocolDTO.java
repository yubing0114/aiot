package com.yb.aiot.module.device.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 添加协议包dto
 * </p>
 *
 * @author cxy
 * @since 2022-12-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProtocolDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("协议插件jar包文件名")
    private String name;

    @ApiModelProperty("协议插件jar包上传后保存路径")
    private String path;

    @ApiModelProperty("协议插件jar包调用id")
    private String pluginId;

    @ApiModelProperty("协议插件描述")
    private String description;
}
