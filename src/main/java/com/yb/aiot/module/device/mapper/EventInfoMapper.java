package com.yb.aiot.module.device.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.aiot.module.device.entity.EventInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.aiot.module.device.entity.dto.QueryEventDTO;
import com.yb.aiot.module.device.entity.vo.EventInSevenDaysVO;
import com.yb.aiot.module.device.entity.vo.EventTableVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
public interface EventInfoMapper extends BaseMapper<EventInfo> {

    /**
     * 分页模糊查询
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<vo.entity.device.module.com.yb.aiot.EventTableVO>
     */
    IPage<EventTableVO> pageListQuery(Page<EventTableVO> page, @Param("query") QueryEventDTO query);


    /**
     * 获取7日内每日事件统计
     * @return com.java.util.List<vo.entity.device.module.com.yb.aiot.EventInSevenDaysVO>
     */
    List<EventInSevenDaysVO> countSevenDaysEvent();


    /**
     * 根据处理状态统计事件数量
     * @return
     */
    Integer countEventByHandleStatus(boolean handleStatus);

    /**
     * 根据eventTypeId List和eventTime查询
     *
     * @param eventTypeIdList eventTypeId List
     * @param eventTime eventTime
     * @return java.util.List<entity.device.module.com.yb.aiot.EventInfo>
     */
    List<EventInfo> selectByEventTypeIdAndEventTime(List<Integer> eventTypeIdList, LocalDateTime eventTime);

}
