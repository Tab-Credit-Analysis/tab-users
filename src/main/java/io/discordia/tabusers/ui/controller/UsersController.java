package io.discordia.tabusers.ui.controller;

import io.discordia.tabusers.service.UsersService;
import io.discordia.tabusers.ui.model.CreateUserRequestModel;
import io.discordia.tabusers.ui.model.CreateUserResponseModel;
import io.discordia.tabusers.users.shared.UserDto;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

  @Autowired
  private Environment env;

  @Autowired
  private UsersService usersService;

  @GetMapping("/status/check")
  public String status() {
    return "Working on port " + env.getProperty("local.server.port") + ", with token = " +env.getProperty("token.secret");
  }

  @PostMapping(
      consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
  )
  public ResponseEntity<CreateUserResponseModel> createUser(
      @Valid @RequestBody CreateUserRequestModel userDetails) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserDto userDto = modelMapper.map(userDetails, UserDto.class);
    UserDto createdUser = usersService.createUser(userDto);
    CreateUserResponseModel returnValue = modelMapper
        .map(createdUser, CreateUserResponseModel.class);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(returnValue);
  }


}
