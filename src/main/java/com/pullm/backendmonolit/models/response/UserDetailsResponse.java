package com.pullm.backendmonolit.models.response;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDetailsResponse {

    private Long id;
    private String fullName;
    private String email;
    private LocalDate birthday;
    private String phone;
    private Boolean isEnabled;

}
