package com.cognizant.feedback.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
public class FeedBackQuestionDTO implements Serializable {

  private static final long serialVersionUID = 9144297938114279163L;

  private Integer questionID;

  @NotEmpty(message = "Provide FeedBack Type")
  private String feedBackType;

  @NotEmpty(message = "Provide Answer Types")
  private List<String> answerTypes;

  @NotEmpty(message = "Provide Question Name")
  private String questionName;

  private Integer totalNoOfAnswers;
  private List<String> answers;
}
