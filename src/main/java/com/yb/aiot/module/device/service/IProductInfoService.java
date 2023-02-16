package com.yb.aiot.module.device.service;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.ProductInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.aiot.module.device.entity.dto.AddProductDTO;
import com.yb.aiot.module.device.entity.dto.UpdateProductDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface IProductInfoService extends IService<ProductInfo> {


    /**
     * 获取所有产品名列表
     * @return
     */
    Result listProductName();

    /**
     * 分页请求产品数据
     * @param pageIndex
     * @param pageSize
     * @return common.com.yb.aiot.Result
     */
    Result selectListPage(Integer pageIndex, Integer pageSize);

    /**
     * 根据Id查询
     * @param productId
     * @return common.com.yb.aiot.Result
     */
    Result selectById(Integer productId);

    /**
     * 根据Id查询详情
     * @param productId
     * @return common.com.yb.aiot.Result
     */
    Result selectDetailById(Integer productId);

    /**
     * 添加产品
     * @param addData
     * @return common.com.yb.aiot.Result
     */
    Result addProduct(AddProductDTO addData);

    /**
     * 修改产品信息
     * @param updateData
     * @return common.com.yb.aiot.Result
     */
    Result updateProduct(UpdateProductDTO updateData);

    /**
     * 根据Id删除
     * @param productId
     * @return common.com.yb.aiot.Result
     */
    Result deleteById(Integer productId);

    /**
     * 获取产品饼图统计
     * @return common.com.yb.aiot.Result
     */
    Result countProductByType();

}
