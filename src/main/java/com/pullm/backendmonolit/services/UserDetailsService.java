package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.models.request.UserDetailsRequest;
import com.pullm.backendmonolit.models.response.UserDetailsResponse;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface UserDetailsService {

    void uploadProfileImage(MultipartFile file) throws IOException;
    void updateUserDetails(UserDetailsRequest userDetailsRequest);
    UserDetailsResponse getUserDetails();

}
