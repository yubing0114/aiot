package com.yb.aiot.module.scheduled.mapper;

import com.yb.aiot.module.scheduled.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 定时任务配置表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-12-06
 */
public interface TaskMapper extends BaseMapper<Task> {

    /**
     * 根据taskKey查询定时任务信息
     *
     * @param taskKey
     * @return java.util.List<entity.scheduled.module.com.yb.aiot.Task>
     */
    List<Task> selectByTaskKey(String taskKey);

    /**
     * 根据defaultTask查询定时任务
     *
     * @param defaultTask defaultTask
     * @return java.util.List<entity.scheduled.module.com.yb.aiot.Task>
     */
    List<Task> selectByDefaultTask(Boolean defaultTask);

}
