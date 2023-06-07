package com.pullm.backendmonolit.models.request;

import com.pullm.backendmonolit.validations.PhoneNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

  @PhoneNumber
  @NotNull(message = "Phone number cannot be null")
  @NotEmpty(message = "Phone number cannot be empty")
  private String phoneNumber;

  @NotEmpty(message = "Password cannot be empty")
  @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
  private String password;
}
