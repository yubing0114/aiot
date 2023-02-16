package com.yb.aiot.module.device.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 选择产品列表VO
 * </p>
 *
 * @author cxy
 * @since 2022-11-21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("产品名")
    private String name;
}
