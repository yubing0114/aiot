package com.yb.aiot.module.device.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.common.utils.MyFileUtil;
import com.yb.aiot.module.device.entity.EventInfo;
import com.yb.aiot.module.device.entity.EventType;
import com.yb.aiot.module.device.entity.dto.ExecuteEventDTO;
import com.yb.aiot.module.device.entity.dto.QueryEventDTO;
import com.yb.aiot.module.device.mapper.EventInfoMapper;
import com.yb.aiot.module.device.service.IEventInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.aiot.module.device.service.IEventTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2022-11-17
 */
@Service
public class EventInfoServiceImpl extends ServiceImpl<EventInfoMapper, EventInfo> implements IEventInfoService {

    @Resource
    private EventInfoMapper eventInfoMapper;

    @Resource
    private IEventTypeService eventTypeService;

    @Override
    public Result pageList(QueryEventDTO queryData){
        return Result.ok(eventInfoMapper.pageListQuery(new Page<>(queryData.getPageIndex(), queryData.getPageSize()), queryData));
    }


    @Override
    public Result executeEvent(ExecuteEventDTO executeData){
        EventInfo eData = getById(executeData.getId());
        if (ObjectUtils.isEmpty(eData)){
            return Result.fail("事件ID不存在");
        }
        eData.setHandleStatus(true);
        eData.setHandleInfo(executeData.getHandleInfo());
        eData.setHandleTime(LocalDateTime.now());
        updateById(eData);
        return Result.ok("处理完毕");
    }

    @Override
    public Result countSevenDaysEvent(){
        return Result.ok(eventInfoMapper.countSevenDaysEvent());
    }

    @Override
    public Result countUnExecuteEvent(){
        return Result.ok(eventInfoMapper.countEventByHandleStatus(false));
    }

    @Override
    public Result deleteByEventSaveDay() {
        List<EventType> eventTypeList = eventTypeService.list();
        Map<Integer, List<Integer>> eventSaveDayMap = new LinkedHashMap<>();
        for (EventType eventType : eventTypeList) {
            Integer eventSaveDay = eventType.getSaveDay();
            if (!ObjectUtils.isEmpty(eventSaveDay)) {
                if (eventSaveDayMap.containsKey(eventSaveDay)) {
                    eventSaveDayMap.get(eventSaveDay).add(eventType.getId());
                } else {
                    List<Integer> idList = new ArrayList<>();
                    idList.add(eventType.getId());
                    eventSaveDayMap.put(eventSaveDay, idList);
                }
            }
        }
        for (Map.Entry<Integer, List<Integer>> entry : eventSaveDayMap.entrySet()) {
            List<EventInfo> eventInfoList = eventInfoMapper.selectByEventTypeIdAndEventTime(entry.getValue(), getLocalDateTime(entry.getKey()));
            for (EventInfo eventInfo : eventInfoList) {
                removeByEventInfo(eventInfo);
            }
        }
        return Result.ok();
    }

    /**
     * 根据id删除事件及事件图片
     *
     * @param eventInfo eventInfo
     */
    public void removeByEventInfo(EventInfo eventInfo) {
        if (removeById(eventInfo)) {
            List<JSONObject> eventDetail = JSONObject.parseArray(eventInfo.getEventDetail(), JSONObject.class);
            for (JSONObject jsonObject : eventDetail) {
                if (jsonObject.getString("type").equals("picture")) {
                    String picture = jsonObject.getString("value").split("down")[1].substring(1);
                    MyFileUtil.deleteFile(picture);
                }
            }
        }
    }

    public LocalDateTime getLocalDateTime(Integer eventSaveDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - eventSaveDay);
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneOffset.systemDefault());
    }

}
