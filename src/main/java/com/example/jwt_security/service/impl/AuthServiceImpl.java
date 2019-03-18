package com.example.jwt_security.service.impl;


import com.example.jwt_security.entity.JwtUser;
import com.example.jwt_security.entity.User;
import com.example.jwt_security.mapper.UserMapper;
import com.example.jwt_security.mapper.UserRoleMapper;
import com.example.jwt_security.service.AuthService;
import com.example.jwt_security.service.UserServiceImpl;
import com.example.jwt_security.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private UserMapper userRepository;

  @Autowired
  private UserRoleMapper userRoleMapper;

  @Autowired
  private UserServiceImpl userService;

  @Value("${jwt.tokenHead}")
  private String tokenHead;

  @Override
  public int register(User userToAdd) {
    String username = userToAdd.getUsername();
    if (userRepository.findByUsername(username) != null) {
      return 0;
    }
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // - Spring Security自带的加密工具
    String rawPassword = userToAdd.getPassword();
    userToAdd.setPassword(encoder.encode(rawPassword));
    int id = userRepository.addUser(userToAdd);
    userRoleMapper.addUserRole(userToAdd.getId(), 2);
    return userToAdd.getId();
  }

  @Override
  public String login(String username, String password) {
    UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username,
        password);
    Authentication authentication = null;
    try {
      authentication = authenticationManager.authenticate(upToken);
    } catch (Exception e) {
      e.printStackTrace();
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    String token = jwtTokenUtil.generateToken(userDetails);
    userService.updateByUserToken(userRepository.findByUsername(username).getId(),
        token); // - 把token存储到 数据库中(可以是缓存)
    return token;
  }

  @Override
  public String refresh(String oldToken) {
    String token = oldToken.substring(tokenHead.length());
    String username = jwtTokenUtil.getUsernameFromToken(token);
    JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
    if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
      String refresh = jwtTokenUtil.refreshToken(token);
      userService.updateByUserToken(userRepository.findByUsername(username).getId(), refresh);
      return refresh;
    }
    return null;
  }
}
