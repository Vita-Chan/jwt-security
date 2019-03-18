package com.example.jwt_security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * springSecurity 规定必须的user类
 */
public class UserDetailsImpl implements UserDetails {

  private final int id;
  private final String username;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;
  private final Date lastPasswordResetDate; //上次密码重置日期

  public UserDetailsImpl(int id, String username, String password,
      Collection<? extends GrantedAuthority> authorities, Date lastPasswordResetDate) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.lastPasswordResetDate = lastPasswordResetDate;
  }

  /**
   * 返回分配给用户的角色列表
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  /**
   * 账户是否未过期
   */
  @JsonIgnore  //将这个bean转化为json的时候 忽略这个属性
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * 账户是否未锁定
   */
  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * 密码是否未过期
   */
  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * 账户是否激活
   */
  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return true;
  }

  public int getId() {
    return this.id;
  }

  public Date getLastPasswordResetDate() {
    return this.lastPasswordResetDate;
  }
}
