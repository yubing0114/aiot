package com.yb.aiot.module.auth.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.module.auth.service.IUserRoleService;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户角色表 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
@Api(tags = "用户权限角色")
@RestController
@RequestMapping("/auth/userRole")
public class UserRoleController {

    @Resource
    private IUserRoleService userRoleService;

    @GetMapping("/getByUserId/{userId}")
    @ApiOperation("根据用户id查询用户的所有权限角色")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result getByUserId(@PathVariable("userId") Integer userId) {
        return userRoleService.selectByUserId(userId);
    }

    @GetMapping("/add/{userId}/{roleId}")
    @ApiOperation("添加用户权限角色")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result add(@PathVariable("userId") Integer userId, @PathVariable("roleId") Integer roleId) {
        return userRoleService.add(userId, roleId);
    }

    @GetMapping("/updateStatus/{userRoleId}")
    @ApiOperation("修改用户权限角色状态")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result updateStatus( @PathVariable("userRoleId") Integer userRoleId) {
        return userRoleService.updateStatus(userRoleId);
    }

}
