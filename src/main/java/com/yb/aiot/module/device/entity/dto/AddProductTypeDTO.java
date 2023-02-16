package com.yb.aiot.module.device.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 添加产品分类dto
 * </p>
 *
 * @author cxy
 * @since 2022-11-17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("上级分类ID")
    private Integer parentTypeId;

    @ApiModelProperty("产品分类名")
    private String name;

    @ApiModelProperty("产品分类编码")
    private String code;

//    @ApiModelProperty("事件信息保存时长")
//    private Integer eventSaveDay;
}
