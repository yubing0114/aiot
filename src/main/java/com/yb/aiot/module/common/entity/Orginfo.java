package com.yb.aiot.module.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 机构信息
 * <p>
 *
 * @author author
 * @date 2022/11/16 15:40
 */
@Data
public class Orginfo {

    @ApiModelProperty("appid")
    public String appid;

    @ApiModelProperty("appkey")
    public String appkey;

}
