package com.pullm.backendmonolit.exception.handling;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponse {

  private String customMessage;
  private List<String> details;

}
