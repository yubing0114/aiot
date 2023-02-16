package com.yb.aiot.module.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.ProductType;
import com.yb.aiot.module.device.entity.dto.AddProductTypeDTO;
import com.yb.aiot.module.device.entity.dto.UpdateProductTypeDTO;
import com.yb.aiot.module.device.mapper.ProductTypeMapper;
import com.yb.aiot.module.device.service.IProductTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements IProductTypeService {

    @Resource
    private ProductTypeMapper productTypeMapper;

    @Override
    public Result selectList() {
        List<ProductType> pTypeList = list();
        return Result.ok(pTypeList);
    }

    @Override
    public Result selectList(Integer pageIndex, Integer pageSize){
        return Result.ok(productTypeMapper.selectPage(new Page<ProductType>(pageIndex, pageSize), null));
    }

    @Override
    public Result add(AddProductTypeDTO addData){
        addData.setName(addData.getName().trim());
        ProductType existsType = selectByTypename(addData.getName());
        if (!ObjectUtils.isEmpty(existsType)) {
            return Result.fail("已存在同名产品分类");
        }
        ProductType pType = ProductType.builder().build();
        BeanUtil.copyProperties(addData,pType);
        save(pType);
        return Result.ok("添加成功");
    }

    @Override
    public Result update(UpdateProductTypeDTO updateData){
        ProductType pData = getById(updateData.getId());
        if (ObjectUtils.isEmpty(pData)){
            return Result.fail("产品分类不存在");
        }
        pData.setName(updateData.getName());
        pData.setParentTypeId(updateData.getParentTypeId());
        pData.setCode(updateData.getCode());
//        pData.setEventSaveDay(updateData.getEventSaveDay());
        updateById(pData);
        return Result.ok("修改成功");
    }

    @Override
    public Result selectById(Integer productTypeId){
        ProductType pData = getById(productTypeId);
        if (ObjectUtils.isEmpty(pData)){
            return Result.fail("产品分类不存在");
        }
        return Result.ok(pData);
    }

    @Override
    public Result listAllTypeByParentId(Integer parentId){
        return Result.ok(productTypeMapper.listByParentId(parentId));
    }

    public Result getConcatTypeNameById(Integer prodcutTypeId){
        return Result.ok(productTypeMapper.selectConcatTypeNameById(prodcutTypeId));
    }

    @Override
    public Result delete(Integer productTypeId){
        ProductType dp = getById(productTypeId);
        if (ObjectUtils.isEmpty(dp)){
            return Result.fail("产品分类不存在");
        }
        List<ProductType> pList = productTypeMapper.selectByParentId(productTypeId);
        for(int i = 0; i < pList.size(); i ++){
            pList.get(i).setParentTypeId(dp.getParentTypeId());
            updateById(pList.get(i));
        }
        removeById(productTypeId);
        return Result.ok("删除成功");
    }

    private ProductType selectByTypename(String typename) {
        List<ProductType> pTypeList = productTypeMapper.selectByTypename(typename);
        return CollectionUtils.isEmpty(pTypeList) ? null : pTypeList.get(0);
    }
}
