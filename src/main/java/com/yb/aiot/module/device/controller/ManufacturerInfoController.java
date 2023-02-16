package com.yb.aiot.module.device.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.dto.AddManufacturerInfoDTO;
import com.yb.aiot.module.device.entity.dto.QueryManufacturerDTO;
import com.yb.aiot.module.device.entity.dto.UpdateManufacturerInfoDTO;
import com.yb.aiot.module.device.service.IManufacturerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2022-12-15
 */
@Api(tags = "厂商信息")
@RestController
@RequestMapping("/device/manufacturer-info")
public class ManufacturerInfoController {

    @Resource
    IManufacturerInfoService manufacturerInfoService;

    @ApiOperation("请求所有厂商信息")
    @GetMapping(value="/select/list")
    @SaCheckPermission(AuthConst.USER)
    public Result listAll(){
        return manufacturerInfoService.listAll();
    }

    @ApiOperation("分页请求厂商信息")
    @PostMapping (value="/select/page")
    @SaCheckPermission(AuthConst.USER)
    public Result pageList(@RequestBody QueryManufacturerDTO queryData){
        return manufacturerInfoService.pageList(queryData);
    }

    @ApiOperation("添加厂商信息")
    @PostMapping(value="/add")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result addData(@RequestBody AddManufacturerInfoDTO addData){
        return manufacturerInfoService.addManufacturer(addData);
    }

    @ApiOperation("修改厂商信息")
    @PostMapping(value="/update")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result updateData(@RequestBody UpdateManufacturerInfoDTO updateData){
        return manufacturerInfoService.updateManufacturer(updateData);
    }

    @ApiOperation("根据Id获取厂商信息")
    @GetMapping(value="/select/{id}")
    @SaCheckPermission(AuthConst.USER)
    public Result selectById(@PathVariable("id") Integer id){
        return manufacturerInfoService.selectById(id);
    }
}
