package com.cognizant.feedback.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExceptionDTO implements Serializable {

  private static final long serialVersionUID = -6883221056574395056L;

  private String errorCode;
  private String errorMsg;
}
