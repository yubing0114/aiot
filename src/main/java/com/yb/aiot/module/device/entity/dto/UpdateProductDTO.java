package com.yb.aiot.module.device.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 添加产品dto
 * </p>
 *
 * @author cxy
 * @since 2022-11-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("产品分类ID")
    private Integer productTypeId;

    @ApiModelProperty("产品名")
    private String name;

    @ApiModelProperty("厂商名")
    private String manufacturerName;

    @ApiModelProperty("生产厂商id")
    private Integer producerId;

    @ApiModelProperty("产品型号")
    private String modelNumber;

    @ApiModelProperty("通信端口号")
    private Integer port;

    @ApiModelProperty("登录设备用户名")
    private String username;

    @ApiModelProperty("登录设备密码")
    private String password;

    @ApiModelProperty("视频流地址(摄像头设备使用)")
    private String streamUrl;
}
