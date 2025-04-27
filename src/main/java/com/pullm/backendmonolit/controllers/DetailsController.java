package com.pullm.backendmonolit.controllers;

import com.pullm.backendmonolit.services.UserDetailsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) throws IOException {
        userDetailsService.uploadProfileImage(file);
        return ResponseEntity.ok("Profile picture uploaded");
    }

}
