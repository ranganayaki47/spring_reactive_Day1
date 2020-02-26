package com.cognizant.feedback.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class UserRequestDTO implements Serializable {

  private static final long serialVersionUID = -6027189630824954098L;

  @NotEmpty(message = "User emailID is not Empty")
  private String emailID;

  @NotEmpty(message = "Password is not Empty")
  private String password;

  @Override
  public String toString() {
    return "UserRequestDTO{"
        + "emailID='"
        + emailID
        + '\''
        + ", password='"
        + password
        + '\''
        + '}';
  }
}
