package com.yb.aiot.module.auth.service;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.auth.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
public interface IRoleService extends IService<Role> {

    /**
     * 查询系统所有权限角色
     *
     * @return common.com.yb.aiot.Result
     */
    Result selectAll();

    /**
     * 添加
     */
    void add();

}
