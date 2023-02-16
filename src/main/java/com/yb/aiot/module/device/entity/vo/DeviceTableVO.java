package com.yb.aiot.module.device.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2022-11-22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTableVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("产品表id")
    private Integer productInfoId;

    @ApiModelProperty("产品名")
    private String productName;

    @ApiModelProperty("产品分类名")
    private String productTypeName;

    @ApiModelProperty("产品型号")
    private String modelNumber;

    @ApiModelProperty("协议表id")
    private Integer protocolInfoId;

    @ApiModelProperty("协议包名")
    private String protocolName;

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("状态(false-异常,true-正常)")
    private Boolean status;

    @ApiModelProperty("在线状态(false-下线,true-在线)")
    private Boolean isOnline;

    @ApiModelProperty("布防状态(false-未布防,true-已布防)")
    private Boolean alarmStatus;

    @ApiModelProperty("是否需要默认布防(false-不需要,true-需要)")
    private Boolean defaultAlarm;

    @ApiModelProperty("设备ip")
    private String ip;

    @ApiModelProperty("设备所在地址")
    private Integer addressId;

    @ApiModelProperty("设备所在地址名")
    private String addressName;

    @ApiModelProperty("标注状态(false-未标注，true-已标注)")
    private Boolean gisStatus;

    @ApiModelProperty("gis信息json格式")
    private String gisInfo;

//    @ApiModelProperty("物模型json格式")
//    private String thingsModel;

}
