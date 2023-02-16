package com.yb.aiot.module.device.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 七日内事件数统计VO
 * </p>
 *
 * @author cxy
 * @since 2022-11-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventInSevenDaysVO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日期名")
    private String days;

    @ApiModelProperty("事件统计数量")
    private Integer count;
}
