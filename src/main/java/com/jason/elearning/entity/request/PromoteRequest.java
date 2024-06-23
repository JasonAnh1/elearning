package com.jason.elearning.entity.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromoteRequest {
    public Long amount;
    public String transCode;
    public Long courseId;
}
