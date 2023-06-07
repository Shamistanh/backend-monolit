package com.pullm.backendmonolit.services.impl;


import static com.pullm.backendmonolit.exception.handling.ExceptionsMessages.DUPLICATE_RESOURCE_EXCEPTION;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.exception.DuplicateResourceException;
import com.pullm.backendmonolit.exception.InvalidOTPException;
import com.pullm.backendmonolit.mapper.UserMapper;
import com.pullm.backendmonolit.models.request.ActiveAccountRequest;
import com.pullm.backendmonolit.models.request.AuthenticationRequest;
import com.pullm.backendmonolit.models.request.RegisterPhoneRequest;
import com.pullm.backendmonolit.models.response.AuthenticationResponse;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.security.jwt.JWTUtil;
import com.pullm.backendmonolit.services.AuthenticationService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserMapper userMapper = UserMapper.INSTANCE;

  private final JWTUtil jwtUtil;
  private final CacheManager cacheManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  @Override
  public AuthenticationResponse register(RegisterPhoneRequest request) {
    log.info("register().start username: {}", request.getUsername());

    var number = request.getPhoneNumber();

    if (userRepository.existsUserByPhoneNumber(number)) {
      log.error("register().error Phone number already exists number");
      throw new DuplicateResourceException(DUPLICATE_RESOURCE_EXCEPTION.getMessage());
    }

    var user = userMapper.mapToUser(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setIsEnabled(Boolean.FALSE);

    userRepository.save(user);
    var jwtToken = jwtUtil.generateToken(user.getPhoneNumber(), "ROLE_USER");

    Objects.requireNonNull(cacheManager.getCache("response")).clear();

    log.info("register().end user-id: {}", user.getId());

    return AuthenticationResponse.builder().token(jwtToken).build();
  }

  @Override
  public AuthenticationResponse login(AuthenticationRequest request) {
    log.info("login().start");

    var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getPhoneNumber(),
            request.getPassword()
        )
    );

    var user = (User) authentication.getPrincipal();

    var roles = user.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    var jwtToken = jwtUtil.generateToken(user.getPhoneNumber(), roles);

    log.info("login().end user-id: {}", user.getId());

    return AuthenticationResponse.builder().token(jwtToken).build();
  }

  @Override
  public void activateAccount(ActiveAccountRequest request) {
    log.info("activateAccount().start");
    var user = userRepository.findUserByPhoneNumber(request.getPhoneNumber())
        .orElseThrow(() -> new UsernameNotFoundException("Phone number not found"));

    if(user.getIsEnabled()) {
      log.error("Invalid user already active");
      throw new InvalidOTPException("Invalid user already active");
    }

    // TODO
//    int otp = (int) (Math.random() * 9000) + 1000;
//    twilloApi.sendMessage(otp)

    var otp = 5555;

    if (request.getOtp() != otp) {
      log.error("Invalid otp: " + request.getOtp());
      throw new InvalidOTPException("Invalid otp");
    }

    user.setIsEnabled(Boolean.TRUE);
    userRepository.save(user);

    log.info("activateAccount().end user id {} successfully activated", user.getId());
  }

}