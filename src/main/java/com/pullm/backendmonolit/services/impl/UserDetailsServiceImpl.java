package com.pullm.backendmonolit.services.impl;

import com.pullm.backendmonolit.entities.User;
import com.pullm.backendmonolit.exception.NotFoundException;
import com.pullm.backendmonolit.models.request.UserDetailsRequest;
import com.pullm.backendmonolit.models.response.UserDetailsResponse;
import com.pullm.backendmonolit.repository.UserRepository;
import com.pullm.backendmonolit.services.UserDetailsService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public void uploadProfileImage(MultipartFile file) throws IOException {
        User user = getUser();
        user.getDetail().setProfileImageId(Arrays.toString(file.getBytes()));
        userRepository.save(user);
        log.info("User profile image uploaded");
    }

    @Override
    public void updateUserDetails(UserDetailsRequest userDetailsRequest) {
        User user = getUser();
        String fullName = userDetailsRequest.getFullName();
        if (fullName != null) {
            user.setFullName(fullName);
        }
        LocalDate birthday = userDetailsRequest.getBirthday();
        if (birthday != null) {
            user.getDetail().setBirthday(birthday);
        }
        userRepository.save(user);
    }

    @Override
    public UserDetailsResponse getUserDetails() {
        User user = getUser();
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
        userDetailsResponse.setFullName(user.getFullName());
        userDetailsResponse.setEmail(user.getEmail());
        userDetailsResponse.setBirthday(user.getDetail() != null ? user.getDetail().getBirthday() : null);
        userDetailsResponse.setId(user.getId());
        userDetailsResponse.setIsEnabled(user.getIsEnabled());
        return userDetailsResponse;
    }


    private User getUser() {
        var number = extractMobileNumber();
        var user = userRepository.findUserByEmail(number)
                .orElseThrow(() -> new NotFoundException("Phone number not found"));

        log.info("getUser(): user-id: " + user.getId());

        return user;
    }

    private String extractMobileNumber() {
        var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

}
