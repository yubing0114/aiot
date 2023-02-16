package com.yb.aiot.module.auth.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 更新用户dto
 * </p>
 *
 * @author author
 * @since 2022-10-27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("头像地址")
    private String avatarUrl;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别(1-男 2-女)")
    private Integer sex;

}
