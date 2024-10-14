package com.translator.up.model.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class RegisterUserRequest {
    private String fullName;
    private String role;
    private String email;
    private String phoneNumber;
    private String password;

}
