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
public class AuthorityController {

  @Autowired
  UserServiceImpl userService;


  /**
   * hasRole 和 hasAuthority的区别
   * hasRole 所针对的是角色.
   *  用法上: hasRole 在数据库中存储的时候必须要加一个作为ROLE_前缀
   * hasAuthority 针对的是权限.
   *  用法上: hasAuthority可以直接使用
   *
   * 角色和权限之间的关系: 一个角色下面可以有多个权限, 一个权限可以同属于多个角色
   * @return
   */
  @GetMapping("/getUserLoginToken")
  //@PreAuthorize("hasRole('USER')")
  @PreAuthorize("hasAuthority('USER')")
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