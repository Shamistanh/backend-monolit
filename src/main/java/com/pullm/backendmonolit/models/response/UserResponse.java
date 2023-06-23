package com.pullm.backendmonolit.models.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String bio;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private LocalDate birthday;
}
