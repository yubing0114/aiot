package com.yb.aiot.module.auth.service.impl;

import com.yb.aiot.module.auth.service.IRoleService;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.auth.entity.Role;
import com.yb.aiot.module.auth.mapper.RoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public Result selectAll() {
        return Result.ok(list());
    }

    @Override
    public void add() {
        if (CollectionUtils.isEmpty(list())) {
            roleMapper.add();
        }
    }

}
