package com.pullm.backendmonolit.controllers;


import com.pullm.backendmonolit.models.request.ActiveAccountRequest;
import com.pullm.backendmonolit.models.request.AuthenticationRequest;
import com.pullm.backendmonolit.models.request.EmailRequest;
import com.pullm.backendmonolit.models.request.ForgetPasswordRequest;
import com.pullm.backendmonolit.models.request.RegisterRequest;
import com.pullm.backendmonolit.models.request.VerifyOtpRequest;
import com.pullm.backendmonolit.models.response.AuthenticationResponse;
import com.pullm.backendmonolit.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auths")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void registerWithNumber(@RequestBody @Valid RegisterRequest request) {
        authenticationService.register(request);
    }

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest request) {
        return authenticationService.login(request);
    }

    @PostMapping("/activate")
    @ResponseStatus(code = HttpStatus.OK)
    public void activateAccount(@RequestBody @Valid ActiveAccountRequest request) {
        authenticationService.activateAccount(request);
    }

    @PostMapping("/reset-password/otp")
    @ResponseStatus(code = HttpStatus.OK)
    public void sendEmail(@RequestBody @Valid EmailRequest request) {
        authenticationService.sendEmail(request);
    }

    @PostMapping("/verify-otp")
    @ResponseStatus(code = HttpStatus.OK)
    public boolean verifyOtp(@RequestBody @Valid VerifyOtpRequest request) {
        return authenticationService.verifyOtp(request);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(code = HttpStatus.OK)
    public void forgetPassword(@RequestBody @Valid ForgetPasswordRequest request) {
        authenticationService.forgetPassword(request);
    }

}