package com.yb.aiot.module.device.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 数据统计VO
 * </p>
 *
 * @author cxy
 * @since 2022-11-24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PieCountVO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("统计项名")
    private String name;

    @ApiModelProperty("统计项对应数量")
    private Integer value;
}
