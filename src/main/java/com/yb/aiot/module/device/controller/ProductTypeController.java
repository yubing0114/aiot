package com.yb.aiot.module.device.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.dto.AddProductTypeDTO;
import com.yb.aiot.module.device.entity.dto.UpdateProductTypeDTO;
import com.yb.aiot.module.device.service.IProductTypeService;
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
@Api(tags = "产品分类")
@RestController
@RequestMapping("/device/product-type")
public class ProductTypeController {

    @Resource
    private IProductTypeService productTypeService;


    @ApiOperation("查询所有产品分类")
    @GetMapping(value = "/select/list")
    @SaCheckPermission(AuthConst.USER)
    public Result list() {
        return productTypeService.selectList();
    }

    @ApiOperation("分页查询产品分类信息")
    @GetMapping(value="/select/page")
    @SaCheckPermission(AuthConst.USER)
    public Result pageList(@PathParam("pageIndex") Integer pageIndex, @PathParam("pageSize") Integer pageSize){
        return productTypeService.selectList(pageIndex, pageSize);
    }

    @ApiOperation("添加产品分类")
    @PostMapping(value = "/add")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result add(@RequestBody AddProductTypeDTO addData){
        return productTypeService.add(addData);
    }

    @ApiOperation("修改产品分类信息")
    @PostMapping(value="/update")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result updateProductType(@RequestBody UpdateProductTypeDTO updateData){
        return productTypeService.update(updateData);
    }

    @ApiOperation("根据父分类ID获取所有产品分类")
    @GetMapping(value="/selectByParentId/{parentTypeId}")
    @SaCheckPermission(AuthConst.USER)
    public Result listAllTypeByParentId(@PathVariable("parentTypeId") Integer parentTypeId){
        return productTypeService.listAllTypeByParentId(parentTypeId);
    }

    @ApiOperation("根据id获取完整分类名")
    @GetMapping(value="/select/concatName/{id}")
    @SaCheckPermission(AuthConst.USER)
    public Result getConcatTypeNameById(@PathVariable("id") Integer productTypeId){
        return productTypeService.getConcatTypeNameById(productTypeId);
    }

    @ApiOperation("根据Id查询")
    @GetMapping(value="/select/{productTypeId}")
    @SaCheckPermission(AuthConst.USER)
    public Result getProductTypeById(@PathVariable("productTypeId") Integer productTypeId){
        return productTypeService.selectById(productTypeId);
    }

    @ApiOperation("根据id删除")
    @GetMapping(value = "/delete/{productTypeId}")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result delete(@PathVariable("productTypeId") Integer productTypeId) {
        return productTypeService.delete(productTypeId);
    }
}
