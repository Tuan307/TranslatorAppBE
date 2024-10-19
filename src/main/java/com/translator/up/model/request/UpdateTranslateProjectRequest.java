package com.translator.up.model.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile translateFile;
}
