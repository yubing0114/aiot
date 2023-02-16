package com.yb.aiot.module.auth.mapper;

import com.yb.aiot.module.auth.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return java.util.List<entity.auth.module.com.yb.aiot.User>
     */
    List<User> selectByUsername(String username);

}
