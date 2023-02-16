package com.yb.aiot.module.device.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.aiot.module.device.entity.ManufacturerInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.aiot.module.device.entity.dto.QueryManufacturerDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-12-15
 */
public interface ManufacturerInfoMapper extends BaseMapper<ManufacturerInfo> {

    /**
     * 根据编码查询厂商信息
     * @param code
     * @return entity.device.module.com.yb.aiot.ManufacturerInfo
     */
    ManufacturerInfo selectByCode(String code);

    /**
     * 分页查询请求厂商信息
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<entity.device.module.com.yb.aiot.ManufacturerInfo>
     */
    IPage<ManufacturerInfo> pageListQuery(Page<ManufacturerInfo> page, @Param("query") QueryManufacturerDTO query);
}
