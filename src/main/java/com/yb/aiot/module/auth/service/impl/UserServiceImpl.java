package com.yb.aiot.module.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.yb.aiot.module.auth.service.IRoleService;
import com.yb.aiot.module.auth.service.IUserRoleService;
import com.yb.aiot.module.auth.service.IUserService;
import com.yb.aiot.module.common.utils.MyFileUtil;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.auth.entity.Role;
import com.yb.aiot.module.auth.entity.User;
import com.yb.aiot.module.auth.entity.dto.AddUser;
import com.yb.aiot.module.auth.entity.dto.LoginUser;
import com.yb.aiot.module.auth.entity.dto.UpdateUser;
import com.yb.aiot.module.auth.mapper.UserMapper;
import com.yb.aiot.module.auth.mapper.UserRoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.aiot.utils.Md5Util;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private IRoleService roleService;

    @Resource
    private IUserRoleService userRoleService;

    @Override
    public Result login(LoginUser loginUser) {
        User authUser = selectByUsername(loginUser.getUsername());
        if (ObjectUtils.isEmpty(authUser)) {
            return Result.fail("用户不存在");
        }
        if (authUser.getStatus() == 2) {
            return Result.fail("异常用户,请联系管理员恢复正常使用!");
        }
        String password = Md5Util.md5(loginUser.getPassword() + authUser.getSalt());
        if (!authUser.getPassword().equals(password)) {
            return Result.fail("用户名或密码错误");
        }
        StpUtil.login(authUser.getId());
        return Result.ok(StpUtil.getTokenInfo());
    }

    @Override
    public Result select(Integer userId) {
        User user = getById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.fail("id不存在");
        }
        return Result.ok(user);
    }

    @Override
    public Result selectList() {
        List<User> userList = list();
        if (CollectionUtils.isEmpty(userList)) {
            return Result.fail("没有任何用户信息,请先添加用户");
        }
        return Result.ok(userList);
    }

    @Override
    public Result add(AddUser addUser) {
        addUser.setUsername(addUser.getUsername().trim());
        User existsUser = selectByUsername(addUser.getUsername());
        if (!ObjectUtils.isEmpty(existsUser)) {
            return Result.fail("用户已存在");
        }
        User user = User.builder().build();
        BeanUtils.copyProperties(addUser, user);
        String salt = Base64Utils.encodeToString(addUser.getUsername().getBytes());
        String password = Md5Util.md5(addUser.getPassword() + salt);
        // 盐值加密
        user.setPassword(password);
        user.setAvatarUrl(MyFileUtil.getDownFileUrl("default-avatar.jpeg"));
        user.setSalt(salt);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        save(user);
        return Result.ok("注册成功");
    }

    @Override
    public void addMaster() {
        roleService.add();
        AddUser master = AddUser.builder().username("master").password("master").build();
        Result result = add(master);
        if (result.getCode() == 200) {
            User existsUser = selectByUsername(master.getUsername());
            List<Role> roleList = roleService.list();
            for (Role role : roleList) {
                assert existsUser != null;
                userRoleService.add(existsUser.getId(), role.getId());
            }
        }
    }

    @Override
    public Result update(UpdateUser updateUser) {
        User user = getById(updateUser.getId());
        if (ObjectUtils.isEmpty(user)) {
            return Result.fail("id不存在");
        }
        user.setAvatarUrl(updateUser.getAvatarUrl());
        user.setPhone(updateUser.getPhone());
        user.setEmail(updateUser.getEmail());
        user.setSex(updateUser.getSex());
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
        return Result.ok("修改成功");
    }

    @Override
    public Result updateStatus(Integer userId) {
        User user = getById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.fail("id不存在");
        }
        user.setStatus(user.getStatus() == 1 ? 2 : 1);
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
        return Result.ok("修改成功");
    }

    @Override
    public Result updatePwd(Integer userId, String password) {
        User user = getById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.fail("id不存在");
        }
        String salt = Base64Utils.encodeToString(user.getUsername().getBytes());
        String newPassword = Md5Util.md5(password + salt);
        // 盐值加密
        user.setPassword(newPassword);
        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
        return Result.ok("修改成功");
    }

    @Override
    public Result delete(Integer userId) {
        if (!removeById(userId)) {
            return Result.fail("id不存在");
        }
        List<Integer> idList = userRoleMapper.selectIdByUserId(userId);
        userRoleMapper.deleteBatchIds(idList);
        return Result.ok("删除成功");
    }

    private User selectByUsername(String username) {
        List<User> userList = userMapper.selectByUsername(username);
        return CollectionUtils.isEmpty(userList) ? null : userList.get(0);
    }

}
