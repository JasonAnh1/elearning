package com.jason.elearning.repository.certificate;

import com.jason.elearning.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate,Long> {
    Certificate findFirstByCode(String code);
    Boolean existsByCode(String code);
    Boolean existsByCourseIdAndUserId(Long courseId,Long userId);
    Certificate findFirstByCourseIdAndUserId(Long courseId,Long userId);
}
