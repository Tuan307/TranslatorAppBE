package com.translator.up.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProjectDTO {
    private Long id;
    private String title;
    private String clientName;
    private String clientPhoneNumber;
    private String clientEmail;
    private String description;
    private String sourceLanguage;
    private String targetLanguage;
    private Double budget;
    private String deadline;
    private String status;
    private String file;
    private String translatedFile;
    private String createdAt;
}
