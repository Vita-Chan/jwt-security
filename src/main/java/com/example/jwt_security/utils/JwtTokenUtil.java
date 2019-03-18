package com.example.jwt_security.utils;

import com.example.jwt_security.entity.JwtUser;
import com.example.jwt_security.entity.User;
import com.example.jwt_security.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * token相关的工具类
 */
@Component
public class JwtTokenUtil implements Serializable {

  private static final long serizlVersionUID = 3301605591108950415L;

  private static final String CLAIM_KEY_USERNAME = "sub";

  private static final String CLAIM_KEY_CREATED = "created";

  @Value("${jwt.secret}")  //从资源文件中取这个值的value  加密的值
  private String secret;

  @Value("${jwt.expiration}") //过期时间
  private Long expiration;

  @Autowired
  private UserMapper userMapper;

  /**
   * 生成jwt的过期时间
   */
  private Date generateExpirationDate() {
    return new Date(System.currentTimeMillis() + expiration * 1000);
  }

  /**
   * 根据token获取用户名
   */
  public String getUsernameFromToken(String token) {
    String username;
    try {
      final Claims claims = getClaimsFromToken(token);
      username = claims.getSubject();
    } catch (Exception e) {
      username = null;
    }
    return username;
  }

  /**
   * 获取token的创建时间
   */
  public Date getCreatedDateFromToken(String token) {
    Date created;
    try {
      final Claims claims = getClaimsFromToken(token);
      created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
    } catch (Exception e) {
      created = null;
    }
    return created;
  }

  /**
   * 生成token
   */
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername()); //标题是用户名
    claims.put(CLAIM_KEY_CREATED, new Date()); //创建时间
    return generateToken(claims);
  }

  String generateToken(Map<String, Object> claims) {
    return Jwts.builder()
        .setClaims(claims)
        .setExpiration(generateExpirationDate())
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  /**
   * 获取token中包含的数据声明(claims)
   */
  public Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser()
          .setSigningKey(secret) //解析token的密码
          .parseClaimsJws(token) //要解析的token
          .getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  /**
   * 获得这个token的过期时间
   */
  public Date getExpirationDateFromToken(String token) {
    Date expiration;
    try {
      final Claims claims = getClaimsFromToken(token);
      expiration = claims.getExpiration();
    } catch (Exception e) {
      expiration = null;
    }
    return expiration;
  }

  /**
   * 判断这个token是否过期
   */
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  /**
   * 判断创建时间是否大于最后一次登录时间
   *
   * @param created 创建时间
   * @param lastPasswordReset 最后一次重置密码的时间
   */
  private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
    return (lastPasswordReset != null && created.before(lastPasswordReset));
  }

  public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
    final Date created = getCreatedDateFromToken(token);
    return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
        && !isTokenExpired(token);
  }

  /**
   * 刷新token
   */
  public String refreshToken(String token) {
    String refreshedToken;
    try {
      final Claims claims = getClaimsFromToken(token);
      claims.put(CLAIM_KEY_CREATED, new Date());
      refreshedToken = generateToken(claims);
    } catch (Exception e) {
      refreshedToken = null;
    }
    return refreshedToken;
  }

  /**
   * 验证token
   *
   * @param userDetails 相当于能获得用户信息
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    JwtUser user = (JwtUser) userDetails;

    String username = getUsernameFromToken(token);
    String userToken = userMapper.findByUsername(username).getToken();
    if (!userToken.equals(token)) {
      return false;
    }
    return (username.equals(user.getUsername()) && !isTokenExpired(token));
  }
}
