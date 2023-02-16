package com.yb.aiot.module.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 事件类型信息表
 * </p>
 *
 * @author author
 * @since 2022-12-15
 */
@Data
@TableName("event_type")
@ApiModel(value = "EventType对象", description = "事件类型信息表")
public class EventType implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("产品类型id")
    private Integer productTypeId;
    @ApiModelProperty("产品信息id")
    private Integer productInfoId;
    @ApiModelProperty("事件类型名称")
    private String name;
    @ApiModelProperty("事件类型码")
    private String code;
    @ApiModelProperty("事件等级1,2,3...(1等级最高，2等级次之，以此类推)")
    private Integer level;
    @ApiModelProperty("事件保存时间(单位:天)，不在保存时间之内的将会被清除")
    private Integer saveDay;

}
