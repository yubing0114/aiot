package com.yb.aiot.module.device.service;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.ManufacturerInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.aiot.module.device.entity.dto.AddManufacturerInfoDTO;
import com.yb.aiot.module.device.entity.dto.QueryManufacturerDTO;
import com.yb.aiot.module.device.entity.dto.UpdateManufacturerInfoDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2022-12-15
 */
public interface IManufacturerInfoService extends IService<ManufacturerInfo> {


    /**
     * 氢气所有厂商信息
     * @return
     */
    Result listAll();

    /**
     * 分页请求厂商信息
     * @param pageIndex
     * @param pageSize
     * @return common.com.yb.aiot.Result
     */
    Result pageList(QueryManufacturerDTO queryData);

    /**
     * 根据id查询厂商信息
     * @param id
     * @return
     */
    Result selectById(Integer id);

    /**
     * 添加厂商信息
     * @param addData
     * @return common.com.yb.aiot.Result
     */
    Result addManufacturer(AddManufacturerInfoDTO addData);

    /**
     * 修改厂商信息
     * @param updateData
     * @return common.com.yb.aiot.Result
     */
    Result updateManufacturer(UpdateManufacturerInfoDTO updateData);
}
