package com.yb.aiot.module.device.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 产品VO
 * </p>
 *
 * @author cxy
 * @since 2022-12-16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductTableVO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("产品分类名")
    private String productTypeName;

    @ApiModelProperty("产品名")
    private String name;

    @ApiModelProperty("生产厂商名")
    private String manufacturerName;

    @ApiModelProperty("产品型号")
    private String modelNumber;
}