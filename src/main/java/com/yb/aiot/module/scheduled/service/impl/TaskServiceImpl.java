package com.yb.aiot.module.scheduled.service.impl;

import com.yb.aiot.module.scheduled.mapper.TaskMapper;
import com.yb.aiot.module.scheduled.service.ITaskService;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.scheduled.entity.Task;
import com.yb.aiot.module.scheduled.entity.dto.AddTaskDto;
import com.yb.aiot.module.scheduled.entity.dto.UpdateTaskDto;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * <p>
 * 定时定时任务配置表 服务实现类
 * </p>
 *
 * @author author
 * @since 2022-12-06
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    // 存储定时任务执行的包装类
    private final HashMap<String, ScheduledFuture<?>> scheduleMap = new HashMap<>();

    @Override
    public Result start(String taskKey) {
        Task task = selectByTaskKey(taskKey);
        if (ObjectUtils.isEmpty(task)) {
            return Result.fail(String.format("taskKey为%s的定时任务不存在", taskKey));
        }
        String start = startTask(task.getTaskKey(), task.getCron());
        if (!ObjectUtils.isEmpty(start)) {
            return Result.fail(start);
        }
        task.setStatus(true);
        task.setStartTime(LocalDateTime.now());
        updateById(task);
        return Result.ok("启动定时任务成功");
    }

    @Override
    public void startDefaultTask() {
        for (Task task : list()) {
            if (task.getStatus()) {
                task.setStatus(false);
                updateById(task);
            }
        }
        List<Task> taskList = baseMapper.selectByDefaultTask(true);
        for (Task task : taskList) {
            String start = startTask(task.getTaskKey(), task.getCron());
            if (ObjectUtils.isEmpty(start)) {
                task.setStatus(true);
                task.setStartTime(LocalDateTime.now());
                updateById(task);
            } else {
                System.out.println(start);
            }
        }
    }

    @Override
    public Result stop(String taskKey) {
        Task task = selectByTaskKey(taskKey);
        if (ObjectUtils.isEmpty(task)) {
            return Result.fail(String.format("taskKey为%s的定时任务不存在", taskKey));
        }
        if (!stopTask(taskKey)) {
            return Result.fail("停止定时任务失败");
        }
        task.setStatus(false);
        updateById(task);
        return Result.ok("停止定时任务成功");
    }

    @Override
    public Result updateCorn(String taskKey, String corn) {
        Task task = selectByTaskKey(taskKey);
        if (ObjectUtils.isEmpty(task)) {
            return Result.fail(String.format("taskKey为%s的定时任务不存在", taskKey));
        }
        task.setCron(corn);
        task.setUpdateTime(LocalDateTime.now());
        if (updateById(task)) {
            startTask(taskKey, corn);
            return Result.ok("修改定时任务时间成功");
        }
        return Result.fail("修改定时任务时间失败");
    }

    @Override
    public Result all() {
        return Result.ok(list());
    }

    @Override
    public Result add(AddTaskDto addTaskDto) {
        if (!ObjectUtils.isEmpty(selectByTaskKey(addTaskDto.getTaskKey()))) {
            return Result.fail(String.format("taskKey为%s的定时任务已存在", addTaskDto.getTaskKey()));
        }
        Task task = new Task();
        BeanUtils.copyProperties(addTaskDto, task);
        task.setStatus(false);
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        if (save(task)) {
            return Result.ok("添加定时任务成功");
        }
        return Result.ok("添加定时任务失败");
    }

    @Override
    public Result update(UpdateTaskDto updateTaskDto) {
        Task task = getById(updateTaskDto.getId());
        if (ObjectUtils.isEmpty(task)) {
            return Result.fail(String.format("id为%s的定时任务不存在", updateTaskDto.getId()));
        }
        String corn = task.getCron();
        task.setName(updateTaskDto.getName());
        task.setCron(updateTaskDto.getCron());
        task.setDepiction(updateTaskDto.getDepiction());
        task.setStatus(updateTaskDto.getStatus());
        task.setDefaultTask(updateTaskDto.getDefaultTask());
        task.setUpdateTime(LocalDateTime.now());
        if (!updateById(task)) {
            return Result.fail("更新定时任务失败");
        }
        if (task.getStatus() && !corn.equals(task.getCron())) {
            startTask(task.getTaskKey(), task.getCron());
        }
        return Result.ok("更新定时任务成功");
    }

    @Override
    public Result delete(Integer taskId) {
        if (ObjectUtils.isEmpty(getById(taskId))) {
            return Result.fail(String.format("id为%s的定时任务不存在", taskId));
        }
        if (removeById(taskId)) {
            return Result.fail("删除定时任务成功");
        }
        return Result.fail("删除定时任务失败");
    }

    /**
     * 根据taskKey查询定时定时任务信息
     *
     * @param taskKey taskKey
     * @return entity.scheduled.module.com.yb.aiot.Task
     */
    public Task selectByTaskKey(String taskKey) {
        List<Task> taskList = baseMapper.selectByTaskKey(taskKey);
        return CollectionUtils.isEmpty(taskList) ? null : taskList.get(0);
    }

    /**
     * 启动定时任务
     *
     * @param taskKey taskKey
     * @param corn    corn
     * @return boolean
     */
    public String startTask(String taskKey, String corn) {
        stopTask(taskKey);
        String msg;
        try {
            // 初始化一个定时任务
            Runnable runnable = (Runnable) Class.forName(taskKey).newInstance();
            // 将定时任务交给定时任务调度器执行
            ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(runnable, new CronTrigger(corn));
            scheduleMap.put(taskKey, schedule);
            return null;
        } catch (InstantiationException e) {
            msg = "InstantiationException";
        } catch (IllegalAccessException e) {
            msg = "IllegalAccessException";
        } catch (ClassNotFoundException e) {
            msg = "ClassNotFoundException";
        } catch (NoClassDefFoundError e) {
            msg = "NoClassDefFoundError";
        }
        String[] taskKeyArr = taskKey.split("\\.");
        return "启动定时任务失败!" + taskKeyArr[taskKeyArr.length - 1] + ":" + msg;
    }

    /**
     * 停止定时任务
     *
     * @param taskKey taskKey
     * @return boolean
     */
    public boolean stopTask(String taskKey) {
        ScheduledFuture<?> schedule = scheduleMap.get(taskKey);
        if (!ObjectUtils.isEmpty(schedule) && schedule.cancel(true)) {
            scheduleMap.remove(taskKey);
            return true;
        }
        return false;
    }

}
