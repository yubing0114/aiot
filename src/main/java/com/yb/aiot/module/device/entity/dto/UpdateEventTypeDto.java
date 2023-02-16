package com.yb.aiot.module.device.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 更新事件类型信息Dto
 * </p>
 *
 * @author author
 * @since 2022-12-15
 */
@Data
@ApiModel(description = "更新事件类型信息Dto")
public class UpdateEventTypeDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id")
    private Integer id;

    @ApiModelProperty("事件等级1,2,3...(1等级最高，2等级次之，以此类推)")
    private Integer level;

    @ApiModelProperty("事件保存时间(单位:天)，不在保存时间之内的将会被清除")
    private Integer saveDay;

}
