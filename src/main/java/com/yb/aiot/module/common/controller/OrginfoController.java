package com.yb.aiot.module.common.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.common.entity.Orginfo;
import com.yb.aiot.module.common.utils.OrginfoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 机构信息 前端控制器
 * <p>
 *
 * @author author
 * @date 2022/11/16 16:05
 */
@Api(tags = "机构")
@RestController
@RequestMapping("/orginfo")
public class OrginfoController {

    @ApiOperation("查询机构信息")
    @GetMapping("/get")
    @SaCheckPermission(AuthConst.MASTER)
    public Result readAppConf() {
        return OrginfoUtil.readAppConf();
    }

    @ApiOperation("初始化机构信息")
    @PostMapping("/conf")
    @SaCheckPermission(AuthConst.MASTER)
    public Result writeAppConf(@RequestBody Orginfo orginfo) {
        return OrginfoUtil.writeAppConf(orginfo);
    }

}
