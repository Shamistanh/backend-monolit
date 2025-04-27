package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.models.request.UserDetailsRequest;
import com.pullm.backendmonolit.models.response.ResponseDTO;
import com.pullm.backendmonolit.models.response.UserDetailsResponse;
import com.pullm.backendmonolit.services.UserDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/details")
public class DetailsController {

    private final UserDetailsService userDetailsService;

    @PostMapping("/upload-profile-picture")
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) throws IOException {
        userDetailsService.uploadProfileImage(file);
        return ResponseDTO.<String>builder()
                .message("Profile picture uploaded")
               .build();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<String> updateUserDetails(@RequestBody UserDetailsRequest userDetailsRequest) {
        userDetailsService.updateUserDetails(userDetailsRequest);
        return ResponseDTO.<String>builder()
                .message("User details updated")
                .build();
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseDTO<UserDetailsResponse> getUserDetails() {
        return ResponseDTO.<UserDetailsResponse>builder()
                .data(userDetailsService.getUserDetails())
                .message("OK")
                .build();
    }


}
