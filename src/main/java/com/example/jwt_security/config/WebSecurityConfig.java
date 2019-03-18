package com.example.jwt_security.config;

import com.example.jwt_security.filter.AuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * AuthenticationProvider 你可以提供一个实现了 AuthenticationProvider 的bean来定制自己的认证机制。下面的例子展示了如何定制认证机制 这仅用于
 * AuthenticationManagerBuilder 不存在的情况下！
 */
@Configuration
@EnableWebSecurity //开启spring security
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启spring security注解的方式
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  CustomAccessDeniedHandler accessDeniedHandler;    // 无权访问返回的JSON 格式数据给前端（否则为 403 html 页面）

  /**
   * 配置AuthenticationManagerBuilder authenticationManagerBuilder.userDetailsService(this.userDetailsService)
   * - 获得一个DaoAuthenticationConfigurer, 见名知意 操作数据库的 .passwordEncoder(new BCryptPasswordEncoder()) -
   * 操作数据对密码的加密算法用Spring Security自带的加密方式
   */
  @Autowired
  public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder
        .userDetailsService(this.userDetailsService)
        .passwordEncoder(new BCryptPasswordEncoder());
  }

  /**
   * - httpSecurity WebSecurityConfigurerAdapter 的默认方法configure, 配置了HttpSecurity 用于设置哪些请求需要认证,
   * 设置基于表单的认证
   */
  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
        .antMatchers(
            HttpMethod.GET,
            "/",
            "/*.html",
            "/favicon.ico",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js"
        ).permitAll()
        .antMatchers("/auth/**").permitAll()
        .anyRequest().authenticated();

    httpSecurity.headers().cacheControl();

    httpSecurity.exceptionHandling().accessDeniedHandler(accessDeniedHandler);  // - 没有权限情况下使用自定义的报错机制给请求者

    httpSecurity.addFilterBefore(authenticationTokenFilterBean(),
        UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public AuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
    return new AuthenticationTokenFilter();
  }

  /**
   * 解决authenticationManager无法注入问题
   */
  @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
