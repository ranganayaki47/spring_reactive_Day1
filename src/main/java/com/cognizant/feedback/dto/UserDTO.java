package com.cognizant.feedback.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

  private static final long serialVersionUID = -6027189630824954098L;

  private Integer employeeID;
  private String emailID;
  private String password;
  private String userName;
  private String firstName;
  private String lastName;
  private Long contactNo;
  private String roleName;
  private String businessUnit;
}
