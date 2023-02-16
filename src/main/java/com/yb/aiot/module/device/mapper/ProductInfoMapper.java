package com.yb.aiot.module.device.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.aiot.module.device.entity.ProductInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.aiot.module.device.entity.vo.PieCountVO;
import com.yb.aiot.module.device.entity.vo.ProductListVO;
import com.yb.aiot.module.device.entity.vo.ProductDetailVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {


    /**
     * 获取所有产品名列表
     * @return java.util.List<vo.entity.device.module.com.yb.aiot.ProductListVO>
     */
    List<ProductListVO> listProductName();

    /**
     * 分页查询产品列表
     * @param page
     * @return com.baomidou.mybatisplus.core.metadata.IPage<entity.device.module.com.yb.aiot.ProductInfo>
     */
    IPage<ProductInfo> pageList(Page<ProductInfo> page);

    /**
     * 根据产品名查询
     * @param name
     * @return java.util.List<entity.device.module.com.yb.aiot.ProductInfo>
     */
    List<ProductInfo> selectByName(String name);

    /**
     * 根据产品分类ID查询
     * @param productTypeId
     * @return java.util.List<entity.device.module.com.yb.aiot.ProductInfo>
     */
    List<ProductInfo> selectByTypeId(Integer productTypeId);


    /**
     * 多表联合查询产品详情
     * @param productId
     * @return com.yslz.aiot.module.device.entity.ProductDetailVO
     */
    ProductDetailVO selectDetailVO(Integer productId);

    /**
     * 获取总分类产品数量统计
     * @return java.util.List<vo.entity.device.module.com.yb.aiot.PieCountVO>
     */
    List<PieCountVO> countProductByType();

}
