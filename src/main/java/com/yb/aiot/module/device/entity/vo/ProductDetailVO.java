package com.yb.aiot.module.device.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 产品详情VO
 * </p>
 *
 * @author cxy
 * @since 2022-11-18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailVO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("产品分类ID")
    private Integer productTypeId;

    @ApiModelProperty("产品分类名")
    private String productTypeName;

    @ApiModelProperty("产品名")
    private String name;

    @ApiModelProperty("生产厂商名")
    private String manufacturerName;

    @ApiModelProperty("生产厂商联系方式")
    private String manufacturerPhone;

    @ApiModelProperty("生产厂商联系地址")
    private String manufacturerAddress;

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

    @ApiModelProperty("设备数量")
    private Integer deviceCount;
}
