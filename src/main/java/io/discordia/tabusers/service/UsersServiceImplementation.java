package io.discordia.tabusers.service;

import io.discordia.tabusers.users.data.UserEntity;
import io.discordia.tabusers.users.data.UsersRepository;
import io.discordia.tabusers.users.shared.UserDto;
import java.util.ArrayList;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImplementation implements UsersService {

  UsersRepository usersRepository;
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UsersServiceImplementation(UsersRepository usersRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.usersRepository = usersRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }


  @Override
  public UserDto createUser(UserDto userDetails) {
    userDetails.setUserId(UUID.randomUUID().toString());
    userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
    usersRepository.save(userEntity);
    UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
    return returnValue;
  }

  @Override
  public UserDto getUserDetailsByEmail(String email) {
    UserEntity userEntity = usersRepository.findByEmail(email);
    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }
    return new ModelMapper().map(userEntity, UserDto.class);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = usersRepository.findByEmail(username);
    if (userEntity == null) {
      throw new UsernameNotFoundException(username);
    }
    return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true,
        true, true, true, new ArrayList<>());
  }
}
