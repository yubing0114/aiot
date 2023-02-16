package com.yb.aiot.module.device.entity.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 修改厂商dto
 * </p>
 *
 * @author cxy
 * @since 2022-12-16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateManufacturerInfoDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("厂商名")
    private String name;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("联系地址")
    private String address;

    @ApiModelProperty("厂商编码")
    private String code;
}
