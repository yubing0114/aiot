package com.yb.aiot.module.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@TableName("event_info")
@ApiModel(value = "EventInfo对象", description = "事件信息表")
public class EventInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("事件所属设备id")
    private Integer deviceInfoId;

    @ApiModelProperty("事件类型id")
    private Integer eventTypeId;

    @ApiModelProperty("事件名称")
    private String name;

    @ApiModelProperty("事件附加详情json格式")
    private String eventDetail;

    @ApiModelProperty("事件发生时间")
    private LocalDateTime eventTime;

    @ApiModelProperty("事件处理状态(false-未处理,true-已处理)")
    private Boolean handleStatus;

    @ApiModelProperty("事件处理信息")
    private String handleInfo;

    @ApiModelProperty("事件处理时间")
    private LocalDateTime handleTime;

}
