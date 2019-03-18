package com.example.jwt_security.config;

import com.google.gson.JsonObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义没权限是的报错机制, 用法就是实现 AccessDeniedHandler接口, 并重写handle方法即可
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("没有权限",1);
    httpServletResponse.getWriter().write(jsonObject.toString());
  }
}
