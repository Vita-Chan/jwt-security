package com.example.jwt_security.factory;

import com.example.jwt_security.entity.JwtUser;
import com.example.jwt_security.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * JwtUser的工厂类
 */
public class JwtUserFactory {
  private JwtUserFactory() {
  }

  public static JwtUser create(User user) {
    return new JwtUser(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        mapToGrantedAuthorities(user.getRoles()),
        user.getLastPasswordResetDate());
  }

  private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
    return authorities.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }
}
