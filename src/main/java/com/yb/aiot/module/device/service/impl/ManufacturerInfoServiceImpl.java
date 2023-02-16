package com.yb.aiot.module.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.device.entity.ManufacturerInfo;
import com.yb.aiot.module.device.entity.dto.AddManufacturerInfoDTO;
import com.yb.aiot.module.device.entity.dto.QueryManufacturerDTO;
import com.yb.aiot.module.device.entity.dto.UpdateManufacturerInfoDTO;
import com.yb.aiot.module.device.mapper.ManufacturerInfoMapper;
import com.yb.aiot.module.device.service.IManufacturerInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2022-12-15
 */
@Service
public class ManufacturerInfoServiceImpl extends ServiceImpl<ManufacturerInfoMapper, ManufacturerInfo> implements IManufacturerInfoService {

    @Resource
    ManufacturerInfoMapper manufacturerInfoMapper;

    @Override
    public Result listAll(){
        return Result.ok(list());
    }


    @Override
    public Result pageList(QueryManufacturerDTO queryData){
        return Result.ok(manufacturerInfoMapper.pageListQuery(new Page<>(queryData.getPageIndex(), queryData.getPageSize()), queryData));
    }

    @Override
    public Result selectById(Integer id){
        ManufacturerInfo mData = getById(id);
        if (ObjectUtils.isEmpty(mData)){
            return Result.fail("厂商信息不存在");
        }
        return Result.ok(getById(id));
    }

    @Override
    public Result addManufacturer(AddManufacturerInfoDTO addData){
        if (!ObjectUtils.isEmpty(manufacturerInfoMapper.selectByCode(addData.getCode()))){
            return Result.fail("已存在相同编码的厂商");
        }
        ManufacturerInfo mData = ManufacturerInfo.builder().build();
        BeanUtil.copyProperties(addData, mData);
        save(mData);
        return Result.ok("添加厂商信息");
    }

    @Override
    public Result updateManufacturer(UpdateManufacturerInfoDTO updateData){
        ManufacturerInfo mData = getById(updateData.getId());
        if (ObjectUtils.isEmpty((mData))){
            return Result.fail("厂商信息不存在");
        }
        ManufacturerInfo exitData = manufacturerInfoMapper.selectByCode(updateData.getCode());
        if (!ObjectUtils.isEmpty(exitData) && !exitData.getId().equals(updateData.getId())){
            return Result.fail("已存在相同编码的厂商");
        }
        mData.setName(updateData.getName());
        mData.setPhone(updateData.getPhone());
        mData.setAddress(updateData.getAddress());
        mData.setCode(updateData.getCode());
        updateById(mData);
        return Result.ok("修改成功");
    }
}
