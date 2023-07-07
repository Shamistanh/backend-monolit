package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.OneTimePassword;
import com.pullm.backendmonolit.services.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public OneTimePassword generateOTP(String email) {
        Random random = new Random();
        int otpValue = random.nextInt(9000) + 1000;
        String otp = String.valueOf(otpValue);

        return OneTimePassword.builder()
                .password(passwordEncoder.encode(otp))
                .expiredAt(Instant.now().plus(5, ChronoUnit.MINUTES))
                .build();
    }

    @Override
    public boolean validateOTP(String email, String otp) {
        return false;
    }
}
