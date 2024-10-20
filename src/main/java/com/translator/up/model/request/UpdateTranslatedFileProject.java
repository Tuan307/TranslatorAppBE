package com.translator.up.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateTranslatedFileProject {
    private Long id;
    private Long userUploadId;
    private String status;
    private MultipartFile translatedFile;
}
