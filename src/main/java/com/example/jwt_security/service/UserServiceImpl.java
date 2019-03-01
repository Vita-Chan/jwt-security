package com.example.jwt_security.service;

import com.example.jwt_security.entity.User;
import com.example.jwt_security.mapper.UserMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

  @Autowired
  private UserMapper userMapper;

  public User findByUsername(String username) {
    return userMapper.findByUsername(username);
  }

  public User findByUserId(int id){
    List<String> strings = userMapper.queryUserRoles(id);
    User user = userMapper.findByUserId(id);
    user.setRoles(strings);
    return user;
  }

  public int updateByUserToken(int id, String token){
    return userMapper.updateByUserToken(id,token);
  }
}
