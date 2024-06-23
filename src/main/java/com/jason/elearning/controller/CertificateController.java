package com.jason.elearning.controller;

import com.jason.elearning.configuration.Translator;
import com.jason.elearning.entity.Certificate;
import com.jason.elearning.entity.response.BaseResponse;
import com.jason.elearning.service.certificate.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@Transactional
public class CertificateController {
    @Autowired
    private CertificateService certificateService;

    @PostMapping("v1/create-certificate")
    public ResponseEntity<?> createCertificate(@RequestBody Certificate request) {
        try {
            if(request == null) {
                throw new Exception(Translator.toLocale("required_fields"));
            }
            return ResponseEntity.ok( certificateService.addCertificate(request.getCourseId(),request.getUserId()));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }

    @GetMapping("v1/publish/check-cert")
    public ResponseEntity<?> checkCert (@RequestParam(required = false) String code) {
        try {

            return ResponseEntity.ok(new BaseResponse("Success", certificateService.getCertificate( code)));
        } catch (Exception ex) {
            return  ResponseEntity.badRequest().body(new BaseResponse(ex.getMessage(), null));
        }
    }
}
