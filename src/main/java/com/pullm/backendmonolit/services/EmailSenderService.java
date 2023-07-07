package com.pullm.backendmonolit.services;

import com.pullm.backendmonolit.entities.User;

public interface EmailSenderService {
    void send(User user, String otp);
}