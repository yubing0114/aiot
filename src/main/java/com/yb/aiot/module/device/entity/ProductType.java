package com.yb.aiot.module.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("product_type")
@ApiModel(value = "ProductType对象", description = "产品分类表")
public class ProductType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("父分类id(0为没有)")
    private Integer parentTypeId;

    @ApiModelProperty("产品分类名称")
    private String name;

    @ApiModelProperty("产品分类编码")
    private String code;

//    @ApiModelProperty("事件信息保存时长")
//    private Integer eventSaveDay;
}
