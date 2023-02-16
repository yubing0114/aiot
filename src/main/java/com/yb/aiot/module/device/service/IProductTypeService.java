package com.yb.aiot.module.device.service;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.ProductType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.aiot.module.device.entity.dto.AddProductTypeDTO;
import com.yb.aiot.module.device.entity.dto.UpdateProductTypeDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface IProductTypeService extends IService<ProductType> {

    /**
     * 查询所有产品分类
     * @return common.com.yb.aiot.Result
     */
    Result selectList();

    /**
     * 分页查询所有产品分类
     * @param pageIndex
     * @param pageSize
     * @return common.com.yb.aiot.Result
     */
    Result selectList(Integer pageIndex, Integer pageSize);

    /**
     * 添加产品分类
     * @return common.com.yb.aiot.Result
     */
    Result add(AddProductTypeDTO addData);

    /**
     * 修改产品分类信息
     * @param updateData
     * @return common.com.yb.aiot.Result
     */
    Result update(UpdateProductTypeDTO updateData);

    /**
     * 根据Id查询
     * @param productTypeId
     * @return common.com.yb.aiot.Result
     */
    Result selectById(Integer productTypeId);

    /**
     * 根据父类ID获取所有产品分类
     * @param parentTypeId
     * @return common.com.yb.aiot.Result
     */
    Result listAllTypeByParentId(Integer parentTypeId);

    /**
     * 根据Id获取完整分类名称
     * @param productTypeId
     * @return common.com.yb.aiot.Result
     */
    Result getConcatTypeNameById(Integer productTypeId);

    /**
     * 根据id删除分类
     * @param productTypeId
     * @return common.com.yb.aiot.Result
     */
    Result delete(Integer productTypeId);
}
