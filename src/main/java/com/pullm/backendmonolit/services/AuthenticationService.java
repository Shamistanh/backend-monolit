package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.request.ActiveAccountRequest;
import com.pullm.backendmonolit.models.request.AuthenticationRequest;
import com.pullm.backendmonolit.models.request.EmailRequest;
import com.pullm.backendmonolit.models.request.ForgetPasswordRequest;
import com.pullm.backendmonolit.models.request.RegisterRequest;
import com.pullm.backendmonolit.models.response.AuthenticationResponse;

public interface AuthenticationService {

  AuthenticationResponse login(AuthenticationRequest request);

  void register(RegisterRequest request);

  void activateAccount(ActiveAccountRequest request);

  void sendEmail(EmailRequest request);

  void forgetPassword(ForgetPasswordRequest request);
}