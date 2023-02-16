package com.yb.aiot.module.device.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 产品分类详情VO
 * </p>
 *
 * @author cxy
 * @since 2022-12-07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeVO implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("父分类id(0为没有)")
    private Integer parentTypeId;

    @ApiModelProperty("产品分类名称")
    private String name;

    @ApiModelProperty("产品分类编码")
    private String code;

    @ApiModelProperty("子分类数量")
    private Integer childCount;

//    @ApiModelProperty("事件信息保存时长")
//    private Integer eventSaveDay;
}
