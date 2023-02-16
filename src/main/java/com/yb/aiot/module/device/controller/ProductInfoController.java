package com.yb.aiot.module.device.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.dto.AddProductDTO;
import com.yb.aiot.module.device.entity.dto.UpdateProductDTO;
import com.yb.aiot.module.device.service.IProductInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Api(tags = "产品")
@RestController
@RequestMapping("/device/product-info")
public class ProductInfoController {

    @Resource
    private IProductInfoService productInfoService;

    @ApiOperation("获取所有产品名称列表")
    @GetMapping(value="/select/listName")
    @SaCheckPermission(AuthConst.USER)
    public Result listProductName(){
        return productInfoService.listProductName();
    }

    @ApiOperation("分页查询产品")
    @GetMapping(value="/select/page")
    @SaCheckPermission(AuthConst.USER)
    public Result pageList(@PathParam("pageIndex") Integer pageIndex, @PathParam("pageSize") Integer pageSize){
        return productInfoService.selectListPage(pageIndex, pageSize);
    }

    @ApiOperation("根据Id查询")
    @GetMapping(value="/select/{productId}")
    @SaCheckPermission(AuthConst.USER)
    public Result getProductById(@PathVariable("productId") Integer productId){
        return productInfoService.selectById(productId);
    }

    @ApiOperation("根据Id获取详情")
    @GetMapping(value="/select/Detail/{productId}")
    @SaCheckPermission(AuthConst.USER)
    public Result getProductDetailById(@PathVariable("productId") Integer productId){
        return productInfoService.selectDetailById(productId);
    }

    @ApiOperation("添加产品")
    @PostMapping(value="/add")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result addProduct(@RequestBody AddProductDTO addData){
        return productInfoService.addProduct(addData);
    }

    @ApiOperation("修改产品信息")
    @PostMapping(value="/update")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result updateProduct(@RequestBody UpdateProductDTO updateData){
        return productInfoService.updateProduct(updateData);
    }

    @ApiOperation("根据Id删除")
    @GetMapping(value="/delete/{productId}")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result deleteById(@PathVariable("productId") Integer productId){
        return productInfoService.deleteById(productId);
    }
}
