/*
 * Copyright (c) 2019, Zhejiang uniview Technologies Co., Ltd. All rights reserved.
 * <http://www.uniview.com/>
 *------------------------------------------------------------------------------
 * Product     :
 * SdkModule Name :
 * Date Created: 2020-04-16 10:57
 * Creator     : s04180
 * Description :
 *
 *------------------------------------------------------------------------------
 * Modification History
 * DATE        NAME             DESCRIPTION
 *------------------------------------------------------------------------------
 *
 *------------------------------------------------------------------------------
 */
package com.yb.aiot.module.sdk.netsdk.uniview.AcsEntrance.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * AccessRecordBackDataDO
 *
 * @author s04180
 * @Description
 */
public class AccessBackDTO {

    public AccessBackDTO(String url, Integer statusCode, Long recordId) {
        this.response = new LapiResponse();
        response.setResponseUrl(url);
        response.setStatusCode(statusCode);
        Data data = new Data(recordId, new Date());
        response.setData(data);
    }

    @JSONField(name = "Response")
    private LapiResponse<Data> response;

    class Data {
        Data(Long recordId, Date time) {
            this.recordId = recordId;
            this.time = time;
        }
        @JSONField(name = "RecordID")
        Long recordId;
        @JSONField(name = "Time", format = "yyyy-MM-dd HH:mm:ss")
        Date time;

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public Long getRecordId() {
            return recordId;
        }

        public void setRecordId(Long recordId) {
            this.recordId = recordId;
        }
    }

    public LapiResponse<Data> getResponse() {
        return response;
    }

    public void setResponse(LapiResponse<Data> response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "AccessBackDO{" +
                "response=" + response +
                '}';
    }
}
