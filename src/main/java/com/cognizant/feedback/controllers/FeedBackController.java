package com.cognizant.feedback.controllers;

import com.cognizant.feedback.dto.FeedBackQuestionDTO;
import com.cognizant.feedback.dto.UserDTO;
import com.cognizant.feedback.dto.UserResponseDTO;
import com.cognizant.feedback.exceptions.FMSException;
import com.cognizant.feedback.service.FeedBackService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class FeedBackController {
  private final FeedBackService feedBackService;

  @PostMapping("/addquestion")
  public ResponseEntity<Object> addQuestion(@RequestBody FeedBackQuestionDTO feedBackQuestionDTO)
      throws FMSException {
    log.debug("Add FeedBack Question by FeedBack Type:" + feedBackQuestionDTO.getFeedBackType());
    UserResponseDTO userResponseDTO = feedBackService.addQuestion(feedBackQuestionDTO);
    return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
  }

  @GetMapping("/fetchquestions")
  public ResponseEntity<List<FeedBackQuestionDTO>> fetchQuestion() throws FMSException {
    log.debug("Fetch FeedBack Questions From DB:");
    List<FeedBackQuestionDTO> feedBackQuestionDTOS = feedBackService.fetchQuestions();
    return new ResponseEntity<>(feedBackQuestionDTOS, HttpStatus.OK);
  }

  @PutMapping("/updatequestion")
  public ResponseEntity<Object> editQuestion(@RequestBody FeedBackQuestionDTO feedBackQuestionDTO)
      throws FMSException {
    log.debug("Update FeedBack Question by QuestionID :" + feedBackQuestionDTO.getQuestionID());
    UserResponseDTO userResponseDTO = feedBackService.updateQuestion(feedBackQuestionDTO);
    return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
  }

  @DeleteMapping("/removequestion/{questionID}")
  public ResponseEntity<Integer> removeQuestion(@PathVariable Integer questionID)
      throws FMSException {
    log.debug("Delete FeedBack Question by QuestionID :" + questionID);
    feedBackService.removeQuestionByID(questionID);
    return new ResponseEntity<>(questionID, HttpStatus.OK);
  }
}
