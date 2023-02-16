package com.yb.aiot.module.device.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 设备列表VO
 * </p>
 *
 * @author cxy
 * @since 2022-11-23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTableVO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("事件所属设备id")
    private Integer deviceInfoId;

    @ApiModelProperty("事件所属设备名")
    private String deviceName;

    @ApiModelProperty("事件名称")
    private String name;

    @ApiModelProperty("事件分类名称")
    private String eventTypeName;

    @ApiModelProperty("事件等级")
    private Integer level;

    @ApiModelProperty("事件附加详情json格式")
    private String eventDetail;

    @ApiModelProperty("事件发生时间")
    private String eventTime;

    @ApiModelProperty("事件处理状态(false-未处理,true-已处理)")
    private Boolean handleStatus;

    @ApiModelProperty("事件处理信息")
    private String handleInfo;

    @ApiModelProperty("事件处理时间")
    private String handleTime;
}
