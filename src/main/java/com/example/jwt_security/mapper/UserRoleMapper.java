package com.example.jwt_security.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper {

  int addUserRole(@Param("userId") int userId, @Param("roleId") int roleId);
}
