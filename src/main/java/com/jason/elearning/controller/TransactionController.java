package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.request.PlaceOrderRequest;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.enroll.EnrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class TransactionController {
    @Autowired
    private EnrollService enrollService;
    @PostMapping("v1/content-payment")
    public ResponseEntity<?> contentPayment(@Valid @RequestBody final PlaceOrderRequest request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( enrollService.placeOrder(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
