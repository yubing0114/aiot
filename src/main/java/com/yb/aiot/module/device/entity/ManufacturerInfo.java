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
 * @since 2022-12-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("manufacturer_info")
@ApiModel(value = "ManufacturerInfo对象", description = "厂商信息表")
public class ManufacturerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("厂商名")
    private String name;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("联系地址")
    private String address;

    @ApiModelProperty("厂商编码")
    private String code;

}
