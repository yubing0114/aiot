package com.yb.aiot.module.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("product_info")
@ApiModel(value = "ProductInfo对象", description = "产品表")
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("产品分类id")
    private Integer productTypeId;

    @ApiModelProperty("产品名称")
    private String name;

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
