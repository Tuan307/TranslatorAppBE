package com.translator.up.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminUpdateUserRequest {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String role;
    private String password;

}
