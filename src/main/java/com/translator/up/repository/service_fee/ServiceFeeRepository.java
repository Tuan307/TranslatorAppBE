package com.translator.up.repository.service_fee;

import com.translator.up.entity.ServiceFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface ServiceFeeRepository extends JpaRepository<ServiceFeeEntity, Long> {
    @Query("SELECT s FROM ServiceFeeEntity s WHERE s.sourceLanguage = :source AND s.targetLanguage = :target")
    Optional<ServiceFeeEntity> findBySourceAndTargetLanguage(@RequestParam("source") String source, @RequestParam("target") String target);
}
