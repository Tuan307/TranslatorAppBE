package com.translator.up.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private String status;
}
