package com.example.jwt_security.controller;

import com.example.jwt_security.service.UserServiceImpl;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class UserApi {

  @Autowired
  UserServiceImpl userService;


  @GetMapping("/getUserLoginToken")
  @PreAuthorize("hasRole('ADMIN')")
  public String getMessage() {
    System.out.println("你已通过验证: ADMIN");
    return "你已通过验证: ADMIN";
  }


  @GetMapping("/getPassToken")
  @PreAuthorize(value = "hasRole('USER') || hasRole('ADMIN')")
  public String getPassToken() {
    System.out.println("你已通过验证: USER");
    return "你已通过验证: USER";
  }


}