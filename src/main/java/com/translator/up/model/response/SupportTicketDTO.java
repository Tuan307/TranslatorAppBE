package com.translator.up.model.response;

import com.translator.up.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SupportTicketDTO {
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private String userName;
    private String userEmail;
    private String phoneNumber;
    private String role;
}
