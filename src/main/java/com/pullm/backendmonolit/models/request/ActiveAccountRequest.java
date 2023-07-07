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
public class ActiveAccountRequest {

  @Email(message = "Email is not valid")
  @NotNull(message = "Email cannot be null")
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  @NotNull(message = "OTP cannot be null")
  private String otp;
}
