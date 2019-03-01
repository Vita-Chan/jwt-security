package com.example.jwt_security.entity;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * lombok注解:
 *
 * @AllArgsConstructor - 会生成一个包含全部变量的构造函数
 * @NoArgsConstructor - 会生成一个无参的构造函数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  int id;
  String username;
  String password;
  String token;
  List<String> roles;

  private Date lastPasswordResetDate;
}
