package com.yb.aiot.module.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.aiot.module.device.service.IProductInfoService;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.ProductInfo;
import com.yb.aiot.module.device.entity.dto.AddProductDTO;
import com.yb.aiot.module.device.entity.dto.UpdateProductDTO;
import com.yb.aiot.module.device.entity.vo.ProductDetailVO;
import com.yb.aiot.module.device.mapper.ProductInfoMapper;
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
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements IProductInfoService {

    @Resource
    private ProductInfoMapper productInfoMapper;

    @Override
    public Result listProductName(){
        return Result.ok(productInfoMapper.listProductName());
    }

    @Override
    public Result selectListPage(Integer pageIndex, Integer pageSize){
        return Result.ok(productInfoMapper.pageList(new Page<>(pageIndex, pageSize)));
    }

    @Override
    public Result selectById(Integer productId){
        ProductInfo pData = getById(productId);
        if (ObjectUtils.isEmpty(pData)){
            return Result.fail("产品不存在");
        }
        return Result.ok(pData);
    }

    @Override
    public Result selectDetailById(Integer productId){
        ProductDetailVO pVO = productInfoMapper.selectDetailVO(productId);
        if (ObjectUtils.isEmpty(pVO)){
            return Result.fail("产品不存在");
        }
        return Result.ok(pVO);
    }

    @Override
    public Result addProduct(AddProductDTO addData){
        addData.setName(addData.getName().trim());
        ProductInfo existsProduct = selectByName(addData.getName());
        if (!ObjectUtils.isEmpty(existsProduct)) {
            return Result.fail("已存在同名产品");
        }
        ProductInfo pData = ProductInfo.builder().build();
        BeanUtil.copyProperties(addData, pData);
        save(pData);
        return Result.ok("添加成功");
    }

    @Override
    public Result updateProduct(UpdateProductDTO updateData){
        ProductInfo pData = getById(updateData.getId());
        if (ObjectUtils.isEmpty(pData)){
            return Result.fail("产品不存在");
        }
        BeanUtil.copyProperties(updateData, pData);
        updateById(pData);
        return Result.ok("修改成功");
    }

    @Override
    public Result deleteById(Integer productId){
        if (!removeById(productId)){
            return Result.fail("产品不存在");
        }
        return Result.ok("删除成功");
    }

    @Override
    public Result countProductByType(){
        return Result.ok(productInfoMapper.countProductByType());
    }


    private ProductInfo selectByName(String name) {
        List<ProductInfo> pDataList = productInfoMapper.selectByName(name);
        return CollectionUtils.isEmpty(pDataList) ? null : pDataList.get(0);
    }

}
