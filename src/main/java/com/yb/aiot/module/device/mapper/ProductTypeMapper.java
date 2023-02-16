package com.yb.aiot.module.device.mapper;

import com.yb.aiot.module.device.entity.ProductType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.aiot.module.device.entity.vo.ProductTypeVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface ProductTypeMapper extends BaseMapper<ProductType> {

    /**
     * 根据产品分类名查询
     * @param name
     * @return java.util.List<entity.device.module.com.yb.aiot.ProductType>
     */
    List<ProductType> selectByTypename(String name);

    /**
     * 根据产品分类名称模糊查询根节点
     * @param name
     * @return
     */
    List<ProductType> selectParentTypeByName(String name);

    /**
     * 根据父级ID查询
     * @param parentTypeId
     * @return java.util.List<vo.entity.device.module.com.yb.aiot.ProductTypeVO>
     */
    List<ProductTypeVO> listByParentId(Integer parentTypeId);

    /**
     * 根据id获取完整分类名称
     * @param id
     * @return
     */
    String selectConcatTypeNameById(Integer id);

    /**
     * 根据父级ID查询
     * @param parentTypeId
     * @return java.util.List<entity.device.module.com.yb.aiot.ProductType>
     */
    List<ProductType> selectByParentId(Integer parentTypeId);

}
