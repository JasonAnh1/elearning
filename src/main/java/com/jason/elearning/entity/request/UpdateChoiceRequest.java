package com.jason.elearning.entity.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateChoiceRequest  implements Serializable {
    public Long id;
    public String content;
    public Long quizzId;

}
