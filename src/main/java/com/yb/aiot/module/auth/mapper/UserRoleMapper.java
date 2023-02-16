package com.yb.aiot.module.auth.mapper;

import com.yb.aiot.module.auth.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户id查询用户的所有权限角色码
     *
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     */
    List<String> selectByUserId(Integer userId);

    /**
     * 根据用户id查询用户的所有权限角色
     *
     * @param userId 用户id
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.String>>
     */
    List<Map<String, String>> selectMapByUserId(Integer userId);

    /**
     * 根据用户id查询用户的所有权限角色id
     *
     * @param userId 用户id
     * @return java.util.List<java.lang.Integer>
     */
    List<Integer> selectIdByUserId(Integer userId);

}
