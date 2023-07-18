package com.pullm.backendmonolit.services.impl;


import static com.pullm.backendmonolit.exception.handling.ExceptionMessage.DUPLICATE_RESOURCE_EXCEPTION;
import static com.pullm.backendmonolit.exception.handling.ExceptionMessage.MISMATCH_EXCEPTION;

import com.pullm.backendmonolit.entities.OneTimePassword;
import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.exception.DuplicateResourceException;
import com.pullm.backendmonolit.exception.MismatchException;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.exception.OtpException;
import com.pullm.backendmonolit.mapper.UserMapper;
import com.pullm.backendmonolit.models.request.ActiveAccountRequest;
import com.pullm.backendmonolit.models.request.AuthenticationRequest;
import com.pullm.backendmonolit.models.request.EmailRequest;
import com.pullm.backendmonolit.models.request.ForgetPasswordRequest;
import com.pullm.backendmonolit.models.request.RegisterRequest;
import com.pullm.backendmonolit.models.response.AuthenticationResponse;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.security.jwt.JWTUtil;
import com.pullm.backendmonolit.services.AuthenticationService;
import com.pullm.backendmonolit.services.EmailSenderService;
import com.pullm.backendmonolit.services.OtpService;
import java.time.Instant;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    private final JWTUtil jwtUtil;
    private final OtpService otpService;
    private final CacheManager cacheManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("register().start username: {}", request.getFullName());

        var email = request.getEmail();

        if (userRepository.existsUserByEmail(email)) {
            log.error("register().error Email already exists number");
            throw new DuplicateResourceException(DUPLICATE_RESOURCE_EXCEPTION.getMessage());
        }

        OneTimePassword otp = otpService.generateOTP(email);

        var user = User.builder()
            .otp(otp)
            .isEnabled(Boolean.FALSE)
            .email(request.getEmail())
            .fullName(request.getFullName())
            .password(passwordEncoder.encode(request.getPassword()))
            .build();

        userRepository.save(user);
        emailSenderService.send(user, otp.getPassword());

        var jwtToken = jwtUtil.generateToken(user.getEmail(), "ROLE_USER");

        Objects.requireNonNull(cacheManager.getCache("response")).clear();

        log.info("register().end user-id: {}", user.getId());
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        log.info("login().start");

        var dbUser = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() ->
            new NotFoundException("Email not found"));

        if (!passwordEncoder.matches(request.getPassword(), dbUser.getPassword())) {
            log.error("Password does not match");
            throw new MismatchException("Password does not match, please try with valid password");
        }

        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var user = (User) authentication.getPrincipal();

        var roles = user.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        var jwtToken = jwtUtil.generateToken(user.getEmail(), roles);

        log.info("login().end user-id: {}", user.getId());

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public void activateAccount(ActiveAccountRequest request) {
        log.info("activateAccount().start");
        var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() ->
            new NotFoundException("Email not found"));

        if (user.getIsEnabled()) {
            log.error("Invalid user already active");
            throw new OtpException("Invalid user already active");
        }

        var otp = user.getOtp();

        if (otp.getExpiredAt().isBefore(Instant.now())) {
            log.error("Otp expired for user-id: {}", user.getId());
            throw new OtpException("Otp expired");
        }

        if (!request.getOtp().equals(otp.getPassword())) {
            log.error("Invalid otp for user-id: {}", user.getId());
            throw new OtpException("Invalid otp");
        }

        user.setIsEnabled(Boolean.TRUE);
        userRepository.save(user);

        log.info("activateAccount().end user id {} successfully activated", user.getId());
    }

    @Override
    @Transactional
    public void sendEmail(EmailRequest request) {
        log.info("sendEmail().start");
        var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() ->
            new NotFoundException("Email not found"));

        OneTimePassword otp = otpService.generateOTP(request.getEmail());
        user.setOtp(otp);
        userRepository.save(user);
        emailSenderService.send(user, otp.getPassword());
        log.info("sendEmail().end");
    }

    @Override
    public void forgetPassword(ForgetPasswordRequest request) {
        log.info("forgetPassword().start");
        var user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() ->
            new NotFoundException("Email not found"));

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new MismatchException(MISMATCH_EXCEPTION.getMessage());
        }

        if (user.getOtp().getExpiredAt().isBefore(Instant.now())) {
            log.error("Otp expired for user-id: {}", user.getId());
            throw new OtpException("Otp expired");
        }

        if (!request.getOtp().equals(user.getOtp().getPassword())) {
            log.error("Invalid otp for user-id: {}", user.getId());
            throw new OtpException("Invalid otp");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("forgetPassword().end");
    }

}
