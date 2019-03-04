package com.example.jwt_security.service;

import com.example.jwt_security.entity.User;
import com.example.jwt_security.factory.JwtUserFactory;
import com.example.jwt_security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 根据spring security要求创建一个获取UserDetailsService的实现类
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserMapper userMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userMapper.findByUsername(username);
    user.setRoles(userMapper.queryUserRoles(user.getId()));
    if (user == null) {
      throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
    } else {
      return JwtUserFactory.create(user);
    }
  }
}
