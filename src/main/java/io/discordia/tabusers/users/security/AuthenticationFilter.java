package io.discordia.tabusers.users.security;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.discordia.tabusers.service.UsersService;
import io.discordia.tabusers.ui.model.LoginRequestModel;
import io.discordia.tabusers.users.shared.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private UsersService usersService;
  private Environment env;

  public AuthenticationFilter(UsersService usersService, Environment env,
      AuthenticationManager authenticationManager) {
    this.usersService = usersService;
    this.env = env;
    super.setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {
      LoginRequestModel creds = new ObjectMapper()
          .readValue(request.getInputStream(), LoginRequestModel.class);

      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              creds.getEmail(),
              creds.getPassword(),
              new ArrayList<>())
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    String userName = ((User) authResult.getPrincipal()).getUsername();
    UserDto userDetails = usersService.getUserDetailsByEmail(userName);
    String token = Jwts.builder()
        .setSubject(userDetails.getUserId())
        .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(
            Objects.requireNonNull(env.getProperty("token.expiration_time")))))
        .signWith(SignatureAlgorithm.ES256, env.getProperty("token.secret"))
        .compact();
    response.addHeader("token", token);
    response.addHeader("UserID", userDetails.getUserId());
  }

}
