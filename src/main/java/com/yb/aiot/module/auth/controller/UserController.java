package com.yb.aiot.module.auth.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.auth.entity.dto.AddUser;
import com.yb.aiot.module.auth.entity.dto.LoginUser;
import com.yb.aiot.module.auth.entity.dto.UpdateUser;
import com.yb.aiot.module.auth.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Resource
    private IUserService userService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginUser user) {
        return userService.login(user);
    }

    @ApiOperation("登出")
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        StpUtil.logoutByTokenValue(request.getHeader("token"));
        return Result.ok();
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public Result register(@RequestBody AddUser user) {
        return userService.add(user);
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @SaCheckPermission(AuthConst.USER)
    public Result update(@RequestBody UpdateUser user) {
        return userService.update(user);
    }

    @ApiOperation("修改用户状态")
    @GetMapping(value = "/updateStatus/{userId}")
    @SaCheckPermission(AuthConst.MASTER)
    public Result updateStatus(@PathVariable("userId") Integer userId) {
        return userService.updateStatus(userId);
    }

    @GetMapping("/updatePwd/{userId}")
    @ApiOperation("修改密码")
    @SaCheckPermission(AuthConst.MASTER)
    public Result add(@PathVariable("userId") Integer userId, @RequestParam("password") String password) {
        return userService.updatePwd(userId, password);
    }

    @ApiOperation("查询所有用户")
    @GetMapping(value = "/select/list")
    @SaCheckPermission(AuthConst.MASTER)
    public Result list() {
        return userService.selectList();
    }

    @ApiOperation("根据id查询")
    @GetMapping(value = "/select/{userId}")
    @SaCheckPermission(AuthConst.USER)
    public Result select(@PathVariable("userId") Integer userId) {
        return userService.select(userId);
    }

    @ApiOperation("根据id删除")
    @GetMapping(value = "/delete/{userId}")
    @SaCheckPermission(AuthConst.MASTER)
    public Result delete(@PathVariable("userId") Integer userId) {
        return userService.delete(userId);
    }

}
