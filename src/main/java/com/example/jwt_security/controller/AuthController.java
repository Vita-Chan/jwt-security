package com.example.jwt_security.controller;

import com.example.jwt_security.entity.JwtAuthenticationRequest;
import com.example.jwt_security.entity.JwtAuthenticationResponse;
import com.example.jwt_security.entity.User;
import com.example.jwt_security.mapper.UserMapper;
import com.example.jwt_security.service.AuthService;
import com.example.jwt_security.service.UserServiceImpl;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @Value("${jwt.header}")
  private String tokenHeader;

  @Autowired
  private AuthService authService;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserServiceImpl userService;

  @PostMapping("/auth/login")
  public ResponseEntity<?> createAuthenticationToken(
      @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
    String token = authService
        .login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    return ResponseEntity.ok(new JwtAuthenticationResponse(token));
  }

  @GetMapping("/auth/refresh")
  public ResponseEntity<?> refreshAndGetAuthenticationToken(
      HttpServletRequest request) throws AuthenticationException {
    String token = request.getHeader(tokenHeader);
    String refreshedToken = authService.refresh(token);
    if (refreshedToken == null) {
      return ResponseEntity.badRequest().body(null);
    } else {
      return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
    }
  }

  @PostMapping(value = "/auth/register")
  public User register(@RequestBody User addedUser) throws AuthenticationException {
    User user = userService.findByUserId(authService.register(addedUser));
    return user;
  }
}
