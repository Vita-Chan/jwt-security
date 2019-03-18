package com.example.jwt_security.service;

import com.example.jwt_security.entity.User;

public interface AuthService {

  /**
   * 注册
   */
  int register(User userToAdd);

  /**
   * 登录
   */
  String login(String username, String password);

  /**
   * 刷新token
   */
  String refresh(String oldToken);
}
