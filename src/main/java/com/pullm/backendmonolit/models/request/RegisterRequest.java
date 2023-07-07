package com.pullm.backendmonolit.models.request;

import com.pullm.backendmonolit.validations.PhoneNumber;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

  @NotEmpty(message = "Username cannot be empty")
  @Size(min = 4, max = 50, message = "Username must be between 2 and 50 characters")
  @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Username must only contain letters and spaces")
  private String username;

  @Email(message = "Email is not valid")
  @NotNull(message = "Email cannot be null")
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  @NotEmpty(message = "Password cannot be empty")
  @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
  private String password;
}
