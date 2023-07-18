package com.pullm.backendmonolit.exception.handling;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponseDetail {

  private String customMessage;
  private Map<String, String> details;
  private LocalDateTime timestamp;
}
