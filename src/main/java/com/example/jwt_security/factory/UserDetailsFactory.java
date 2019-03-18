package com.example.jwt_security.factory;

import com.example.jwt_security.entity.UserDetailsImpl;
import com.example.jwt_security.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * JwtUser的工厂类
 */
public class UserDetailsFactory {

  private UserDetailsFactory() {
  }

  public static UserDetailsImpl create(User user) {
    return new UserDetailsImpl(
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
