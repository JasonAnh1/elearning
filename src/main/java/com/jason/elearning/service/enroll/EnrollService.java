package com.jason.elearning.service.enroll;

import com.jason.elearning.entity.Enroll;
import com.jason.elearning.entity.request.PlaceOrderRequest;

import java.util.List;

public interface EnrollService {
    List<Enroll> placeOrder(PlaceOrderRequest request) throws Exception;
}
