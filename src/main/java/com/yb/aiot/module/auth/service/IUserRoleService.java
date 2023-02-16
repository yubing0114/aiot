package com.yb.aiot.module.auth.service;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.auth.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 根据用户id查询用户的所有权限角色
     *
     * @param userId 用户id
     * @return common.com.yb.aiot.Result
     */
    Result selectByUserId(Integer userId);

    /**
     * 添加用户权限角色
     *
     * @param userId 用户id
     * @param roleId 权限角色id
     * @return common.com.yb.aiot.Result
     */
    Result add(Integer userId, Integer roleId);

    /**
     * 修改用户权限角色状态
     *
     * @param userRoleId 用户权限角色id
     * @return common.com.yb.aiot.Result
     */
    Result updateStatus(Integer userRoleId);

}
