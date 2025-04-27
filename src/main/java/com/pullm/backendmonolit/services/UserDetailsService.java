package com.pullm.backendmonolit.services;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface UserDetailsService {

    void uploadProfileImage(MultipartFile file) throws IOException;

}
