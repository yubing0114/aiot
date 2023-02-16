package com.yb.aiot.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.yb.aiot.module.auth.mapper.UserRoleMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义Sa-Token权限认证接口扩展
 *
 * @author kong
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    UserRoleMapper userRoleMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        if (loginType.equals(StpUtil.TYPE)) {
            return userRoleMapper.selectByUserId((Integer.parseInt(loginId.toString())));
        }
        return null;
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }

}
