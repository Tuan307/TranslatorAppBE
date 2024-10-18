package com.translator.up.model.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateTranslateProjectRequest {
    private Long id;
    private String title;
    private String description;
    private String sourceLanguage;
    private String targetLanguage;
    private Double budget;
    private String deadline;
    private String translateFile;
}
