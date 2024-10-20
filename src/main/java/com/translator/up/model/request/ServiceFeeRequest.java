package com.translator.up.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ServiceFeeRequest {
    private Double baseFee;
    private String originLanguage;
    private String targetLanguage;
}
