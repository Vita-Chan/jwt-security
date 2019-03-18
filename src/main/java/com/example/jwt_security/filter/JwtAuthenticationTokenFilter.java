package com.example.jwt_security.filter;

import com.example.jwt_security.utils.JwtTokenUtil;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * OncePerRequestFilter - 见名知意, 每一次请求的Filter
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Value("${jwt.header}")
  private String tokenHeader;

  @Value("${jwt.tokenHead}")
  private String tokenHead;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    String authHeader = request.getHeader(this.tokenHeader);
    if (authHeader != null && authHeader.startsWith(tokenHead)) {
      String authToken = authHeader.substring(tokenHead.length()); // - 截取前面的token头部信息 获取token
      String username = jwtTokenUtil.getUsernameFromToken(authToken); // - 通过这个token获取用户名
      logger.info("checking authentication: " + username);

      if (username != null && SecurityContextHolder.getContext().getAuthentication()
          == null) {  // - 就是说这个用户不等于null, 但是没有进行身份验证
        UserDetails userDetails = this.userDetailsService
            .loadUserByUsername(username); // - 通过用户名加载出用户详细信息UserDetails

        if (jwtTokenUtil.validateToken(authToken, userDetails)) { // - 验证token 如果通过
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities()); // - 根据用户的信息和所属权限的信息进行验证
          authenticationToken
              .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          logger.info("authenticated user " + username + ", setting security context");
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
    }
    chain.doFilter(request, response);
  }
}
