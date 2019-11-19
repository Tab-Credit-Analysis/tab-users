package io.discordia.tabusers.service;

import io.discordia.tabusers.users.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {

  UserDto createUser(UserDto userDetails);

  UserDto getUserDetailsByEmail(String email);
}
