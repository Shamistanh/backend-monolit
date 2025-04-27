package com.pullm.backendmonolit.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDetailsRequest {
    private String fullName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" )
    private LocalDate birthday;
}
