package io.discordia.tabusers.ui.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.lang.NonNull;

public class CreateUserRequestModel {

  @NotNull
  @Size(min = 2, message = "First name must be more than 2 characters")
  private String firstName;
  @NotNull
  @Size(min = 2, message = "Last name must be more than 2 characters")
  private String lastName;
  @NotNull(message = "Password cannot be null")
  @Size(min = 8, max = 16, message = "passsword must be greater than 8 and less than 16 characters")
  private String password;

  @NonNull
  @Email
  private String email;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
