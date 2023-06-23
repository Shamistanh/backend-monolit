package com.pullm.backendmonolit.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {


  @GetMapping
  public String test() {
    return "Welcome to secure api";
  }

}
