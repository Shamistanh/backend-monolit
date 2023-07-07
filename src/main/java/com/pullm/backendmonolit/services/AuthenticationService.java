package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.request.ActiveAccountRequest;
import com.pullm.backendmonolit.models.request.AuthenticationRequest;
import com.pullm.backendmonolit.models.request.RegisterRequest;
import com.pullm.backendmonolit.models.response.AuthenticationResponse;

public interface AuthenticationService {

  AuthenticationResponse login(AuthenticationRequest request);

  AuthenticationResponse register(RegisterRequest request);

  void activateAccount(ActiveAccountRequest request);
}