package com.pullm.backendmonolit.models.request;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDetailsRequest {
    private String fullName;
    private LocalDate birthday;
}
