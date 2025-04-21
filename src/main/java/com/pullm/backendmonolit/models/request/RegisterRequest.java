package com.pullm.backendmonolit.models.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "Full name cannot be empty")
    @Size(min = 4, max = 50, message = "Full name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Full name must only contain letters and spaces")
    private String fullName;

    @Email(message = "Email is not valid")
    @NotNull(message = "Email cannot be null")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    private String currency;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;
}
