package com.translator.up.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private String createdAt;
    private Boolean hasRead;
    private String senderName;
    private String projectTitle;
}
