package com.cognizant.feedback.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "feedbackquestion")
public class FeedBackQuestionEntity extends AuditEntity implements Serializable {

  private static final long serialVersionUID = -1373721190486392979L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "questionID")
  private Integer questionID;

  @Column(name = "feedBackType")
  private String feedBackType;

  @Column(name = "answerTypes")
  @ElementCollection(targetClass = String.class)
  private List<String> answerTypes;

  @Column(name = "questionName")
  private String questionName;

  @Column(name = "totalNoOfAnswers")
  private Integer totalNoOfAnswers;

  @Column(name = "answers")
  @ElementCollection(targetClass = String.class)
  private List<String> answers;
}
