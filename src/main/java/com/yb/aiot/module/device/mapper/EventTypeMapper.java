package com.yb.aiot.module.device.mapper;

import com.yb.aiot.module.device.entity.EventType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 事件类型信息表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-12-15
 */
public interface EventTypeMapper extends BaseMapper<EventType> {

    /**
     * 根据productInfoId和name查询
     *
     * @param productInfoId 产品信息id
     * @param name          事件类型名称
     * @return java.util.List<entity.device.module.com.yb.aiot.EventType>
     */
    List<EventType> selectByProductTypeIdAndName(Integer productInfoId, String name);

}
