package com.example.jwt_security.mapper;

import com.example.jwt_security.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
  User findByUsername(@Param("username") String username);

  User findByUserId(@Param("id") int id);

  int updateByUserToken(@Param("id") int id, @Param("token") String token);

  int addUser(User user);

  List<String> queryUserRoles(@Param("id") int id);
}
