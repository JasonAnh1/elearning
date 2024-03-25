package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.request.PlaceOrderRequest;
import com.jason.elearning.entity.request.VerifyRequest;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.enroll.EnrollService;
import com.jason.elearning.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
@Transactional
public class TransactionController {
    @Autowired
    private EnrollService enrollService;
    @Autowired
    private TransactionService transactionService;

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

    @PostMapping("v1/verify-transaction")
    public ResponseEntity<?> verify(@Valid @RequestBody final VerifyRequest request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( transactionService.saveVerify(request));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @GetMapping("v1/publish/test")
    public ResponseEntity<?> revenueByYear(@RequestParam int year) {
        try {

            return ResponseEntity.ok( transactionService.getTotalAmountPerMonth(year));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/revenue/month")
    public ResponseEntity<?> revenueByMonth(@RequestParam int year,@RequestParam int month) {
        try {

            return ResponseEntity.ok( transactionService.getTotalAmountPerDay(year,month));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/revenue/grossProfit")
    public ResponseEntity<?> revenueGrossProfit(@RequestParam int year) {
        try {

            return ResponseEntity.ok( transactionService.revenueGrossProfit(year));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
    @GetMapping("v1/revenue/netProfit")
    public ResponseEntity<?> revenueNetProfit(@RequestParam int year) {
        try {

            return ResponseEntity.ok( transactionService.revenueNetProfit(year));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
