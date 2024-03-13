package com.jason.elearning.entity.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PlaceOrderRequest {
 public List<ItemOrder> items;
 public Long total;
 public String transCode;

}
