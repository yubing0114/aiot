package com.yb.aiot.module.device.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 处理事件dto
 * </p>
 *
 * @author cxy
 * @since 2022-11-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteEventDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("事件ID")
    private Integer id;

    @ApiModelProperty("事件处理信息")
    private String handleInfo;

}
