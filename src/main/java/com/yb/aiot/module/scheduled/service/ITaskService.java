package com.yb.aiot.module.scheduled.service;

import com.yb.aiot.common.Result;
import com.yb.aiot.module.scheduled.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.aiot.module.scheduled.entity.dto.AddTaskDto;
import com.yb.aiot.module.scheduled.entity.dto.UpdateTaskDto;

/**
 * <p>
 * 定时任务配置表 服务类
 * </p>
 *
 * @author author
 * @since 2022-12-06
 */
public interface ITaskService extends IService<Task> {

    /**
     * 启动定时任务
     *
     * @param taskKey taskKey
     * @return common.com.yb.aiot.Result
     */
    Result start(String taskKey);

    /**
     * 启动默认启动的定时任务
     */
    void startDefaultTask();

    /**
     * 停止定时任务
     *
     * @param taskKey taskKey
     * @return common.com.yb.aiot.Result
     */
    Result stop(String taskKey);

    /**
     * 修改定时任务时间
     *
     * @param taskKey taskKey
     * @param corn    corn表达式
     * @return common.com.yb.aiot.Result
     */
    Result updateCorn(String taskKey, String corn);

    /**
     * 查询所有定时任务
     *
     * @return common.com.yb.aiot.Result
     */
    Result all();

    /**
     * 添加定时任务
     *
     * @param addTaskDto 添加定时任务dto
     * @return common.com.yb.aiot.Result
     */
    Result add(AddTaskDto addTaskDto);

    /**
     * 更新定时任务
     *
     * @param updateTaskDto 更新定时任务dto
     * @return common.com.yb.aiot.Result
     */
    Result update(UpdateTaskDto updateTaskDto);

    /**
     * 根据id删除定时任务
     *
     * @param taskId taskId
     * @return common.com.yb.aiot.Result
     */
    Result delete(Integer taskId);

}
