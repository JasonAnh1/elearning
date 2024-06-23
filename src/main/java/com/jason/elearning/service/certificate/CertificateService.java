package com.jason.elearning.service.certificate;


import com.jason.elearning.entity.Certificate;

public interface CertificateService {
    Certificate addCertificate(Long courseId, Long userId) throws Exception;
    Certificate getCertificate(String code);
}
