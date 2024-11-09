package com.translator.up.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserRequest {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
}
