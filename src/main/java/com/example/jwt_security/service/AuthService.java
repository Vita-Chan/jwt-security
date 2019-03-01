package com.example.jwt_security.service;

import com.example.jwt_security.entity.User;

public interface AuthService {

  /**
   * 注册
   * @param userToAdd
   * @return
   */
  int register(User userToAdd);

  /**
   * 登录
   * @param username
   * @param password
   * @return
   */
  String login(String username, String password);

  /**
   * 刷新token
   * @param oldToken
   * @return
   */
  String refresh(String oldToken);
}
