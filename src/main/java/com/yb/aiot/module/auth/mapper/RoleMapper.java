package com.yb.aiot.module.auth.mapper;

import com.yb.aiot.module.auth.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
public interface RoleMapper extends BaseMapper<Role> {

    void add();

}
