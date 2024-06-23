package com.jason.elearning.service.certificate;

import com.jason.elearning.entity.Certificate;
import com.jason.elearning.repository.certificate.CertificateRepository;
import com.jason.elearning.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateServiceImpl implements CertificateService{

    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public Certificate addCertificate(Long courseId, Long userId) throws Exception {
        String randomCode;
        if(certificateRepository.existsByCourseIdAndUserId(courseId, userId)){
            return certificateRepository.findFirstByCourseIdAndUserId(courseId,userId);
        }
        do {
            randomCode = Util.randomAlphaNumeric(7, "");
        } while (certificateRepository.existsByCode(randomCode));

        Certificate certificate = new Certificate();
        certificate.setCode(randomCode);
        certificate.setCourseId(courseId);
        certificate.setUserId(userId);

        return certificateRepository.save(certificate);
    }

    @Override
    public Certificate getCertificate(String code) {

        return certificateRepository.findFirstByCode(code);
    }
}
