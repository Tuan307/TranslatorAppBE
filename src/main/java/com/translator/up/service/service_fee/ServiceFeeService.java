package com.translator.up.service.service_fee;

import com.translator.up.entity.ServiceFeeEntity;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.ServiceFeeRequest;
import com.translator.up.repository.service_fee.ServiceFeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceFeeService {
    @Autowired
    private ServiceFeeRepository serviceFeeRepository;

    public ApiResponse<Double> getServiceFee(String source, String target) {
        Optional<ServiceFeeEntity> entity = serviceFeeRepository.findBySourceAndTargetLanguage(source, target);
        return entity.map(serviceFeeEntity -> new ApiResponse<>("error", "Successful", serviceFeeEntity.getBaseFee(), "")).orElseGet(() -> new ApiResponse<>("error", "Can not find base fee", null, "400"));
    }

    public ApiResponse<ServiceFeeEntity> addBaseFee(ServiceFeeRequest request) {
        Optional<ServiceFeeEntity> entity = serviceFeeRepository.findBySourceAndTargetLanguage(request.getOriginLanguage(), request.getTargetLanguage());
        entity.ifPresent(serviceFeeEntity -> serviceFeeRepository.deleteById(serviceFeeEntity.getId()));
        ServiceFeeEntity model = new ServiceFeeEntity();
        model.setBaseFee(request.getBaseFee());
        model.setSourceLanguage(request.getOriginLanguage());
        model.setTargetLanguage(request.getTargetLanguage());
        serviceFeeRepository.save(model);
        return new ApiResponse<>("error", "Successful", model, "");
    }
}
