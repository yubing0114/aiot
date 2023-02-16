package com.yb.aiot.module.scheduled.controller;

import com.yb.aiot.module.scheduled.service.ITaskService;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.scheduled.entity.dto.AddTaskDto;
import com.yb.aiot.module.scheduled.entity.dto.UpdateTaskDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 定时任务配置表 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-12-06
 */
@Api(tags = "定时任务")
@RestController
@RequestMapping("/scheduled/task")
public class TaskController {

    @Resource
    private ITaskService taskService;

    @ApiOperation("启动定时任务")
    @GetMapping("/start/{taskKey}")
    public Result start(@PathVariable("taskKey") String taskKey) {
        return taskService.start(taskKey);
    }

    @ApiOperation("停止定时任务")
    @GetMapping("/stop/{taskKey}")
    public Result stop(@PathVariable("taskKey") String taskKey) {
        return taskService.stop(taskKey);
    }

    @ApiOperation("修改定时任务时间")
    @GetMapping("/updateCorn/{taskKey}")
    public Result updateCorn(@PathVariable("taskKey") String taskKey, @RequestParam("corn") String corn) {
        return taskService.updateCorn(taskKey, corn);
    }

    @ApiOperation("添加定时任务")
    @PostMapping("/add")
    public Result updateCorn(@RequestBody AddTaskDto addTaskDto) {
        return taskService.add(addTaskDto);
    }

    @ApiOperation("更新定时任务")
    @PostMapping("/update")
    public Result updateCorn(@RequestBody UpdateTaskDto updateTaskDto) {
        return taskService.update(updateTaskDto);
    }

    @ApiOperation("查询所有定时任务")
    @GetMapping("/list")
    public Result updateCorn() {
        return taskService.all();
    }

    @ApiOperation("根据id删除定时任务")
    @GetMapping("/delete/{taskId}")
    public Result updateCorn(@PathVariable("taskId") Integer taskId) {
        return taskService.delete(taskId);
    }

}
