package com.translator.up.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProjectRequest {
    private String title;
    private Long clientId;
    private String description;
    private String sourceLanguage;
    private String targetLanguage;
    private Double budget;
    private String deadline;
    private String status;
    private String file;
    private String createdAt;
}
