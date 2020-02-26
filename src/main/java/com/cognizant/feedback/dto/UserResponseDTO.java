package com.cognizant.feedback.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserResponseDTO implements Serializable {

  private static final long serialVersionUID = 7750601514739490413L;

  private Object successMsg;
  private Object exceptionMsg;
  private Object errorMsg;
  private String userRole;

  @Override
  public String toString() {
    return "UserResponseDTO{"
        + "successMsg="
        + successMsg
        + ", exceptionMsg="
        + exceptionMsg
        + ", errorMsg="
        + errorMsg
        + ", userRole='"
        + userRole
        + '\''
        + '}';
  }
}
