package com.jason.elearning.entity.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse implements Serializable {

    private Object body;
    private String message;
    private Long totalRecord;

    public BaseResponse(String message) {
        this.message = message;
    }


    public BaseResponse(String message, Object body) {
        this.message = message;
        this.body = body;
    }

    public BaseResponse(String message, Object body, long totalRecord) {
        this.message = message;
        this.body = body;
        this.totalRecord = totalRecord;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
    }
}
