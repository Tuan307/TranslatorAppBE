package com.translator.up.controller;

import com.translator.up.aop.SessionRequired;
import com.translator.up.entity.ServiceFeeEntity;
import com.translator.up.model.common.ApiResponse;
import com.translator.up.model.request.ServiceFeeRequest;
import com.translator.up.service.service_fee.ServiceFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/language/fee")
public class ServiceFeeController {

    @Autowired
    private ServiceFeeService service;

    @SessionRequired
    @GetMapping("")
    public ApiResponse<Double> getAllPersonalReviews(@RequestParam(name = "originLanguage") String source, @RequestParam("targetLanguage") String target) {
        return service.getServiceFee(source, target);
    }

    @SessionRequired
    @PostMapping("/create")
    public ApiResponse<ServiceFeeEntity> addBaseFee(@RequestBody ServiceFeeRequest request) {
        return service.addBaseFee(request);
    }
}
