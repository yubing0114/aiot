package com.yb.aiot.module.auth.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.auth.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
@RestController
@RequestMapping("/auth/role")
@Api(tags = "系统权限")
public class RoleController {

    @Resource
    private IRoleService roleService;

    @GetMapping("/all")
    @ApiOperation("查询系统所有权限")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result all() {
        return roleService.selectAll();
    }

}
