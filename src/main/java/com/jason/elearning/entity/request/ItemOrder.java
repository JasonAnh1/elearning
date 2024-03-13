package com.jason.elearning.entity.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class ItemOrder implements Serializable {
    public long id;
    public String type;
}
