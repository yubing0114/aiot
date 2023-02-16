package com.yb.aiot.module.device.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 查询厂商dto
 * </p>
 *
 * @author cxy
 * @since 2022-12-16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryManufacturerDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("当前页数")
    private Integer pageIndex;

    @ApiModelProperty("每页个数")
    private Integer pageSize;

    @ApiModelProperty("厂商名")
    private String name;

    @ApiModelProperty("厂商编码")
    private String code;
}
