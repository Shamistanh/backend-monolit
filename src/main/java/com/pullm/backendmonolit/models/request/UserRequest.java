package com.pullm.backendmonolit.models.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String bio;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private LocalDate birthday;
}
