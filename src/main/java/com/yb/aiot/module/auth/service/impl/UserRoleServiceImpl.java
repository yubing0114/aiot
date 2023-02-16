package com.yb.aiot.module.auth.service.impl;

import com.yb.aiot.module.auth.service.IUserRoleService;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.auth.entity.UserRole;
import com.yb.aiot.module.auth.mapper.UserRoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    public Result selectByUserId(Integer userId) {
        List<Map<String, String>> roleList = userRoleMapper.selectMapByUserId(userId);
        if (CollectionUtils.isEmpty(roleList)) {
            return Result.fail("该用户还未分配权限角色");
        }
        return Result.ok(roleList);
    }

    public Result add(Integer userId, Integer roleId) {
        UserRole userRole = UserRole.builder().userId(userId).roleId(roleId).status(1)
                .createTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).build();
        if (save(userRole)) {
            return Result.ok("添加成功");
        }
        return Result.fail("添加失败");
    }

    public Result updateStatus(Integer userRoleId) {
        UserRole userRole = getById(userRoleId);
        if (ObjectUtils.isEmpty(userRole)) {
            return Result.fail("id不存在");
        }
        userRole.setStatus(userRole.getStatus() == 1 ? 2 : 1);
        userRole.setUpdateTime(LocalDateTime.now());
        updateById(userRole);
        return Result.ok("更新成功");
    }

}
