package com.cognizant.feedback.service;

import com.cognizant.feedback.dto.FeedBackQuestionDTO;
import com.cognizant.feedback.dto.UserResponseDTO;
import com.cognizant.feedback.entity.FeedBackQuestionEntity;
import com.cognizant.feedback.exceptions.FMSException;
import com.cognizant.feedback.mapper.FeedBackQuestionMapper;
import com.cognizant.feedback.repository.FeedBackQuestionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.cognizant.feedback.constants.ErrorConstants.ERROR_SAVING_DATA_TO_DB;
import static com.cognizant.feedback.constants.ErrorConstants.NO_DATA_IN_DB;

@Service
@AllArgsConstructor
@Slf4j
public class FeedBackService {

  private final FeedBackQuestionRepository questionRepository;
  private final FeedBackQuestionMapper questionMapper;

  @Transactional
  public UserResponseDTO addQuestion(FeedBackQuestionDTO feedBackQuestionDTO) throws FMSException {
    log.debug("Save Feed Back Question in DB");
    UserResponseDTO userResponseDTO = new UserResponseDTO();
    boolean isQuestionSaved = saveFeedBackQuestionEntity(feedBackQuestionDTO);
    if (isQuestionSaved) {
      userResponseDTO.setSuccessMsg("Feed Back question addedd successfully");
    } else {
      userResponseDTO.setErrorMsg("Error while saving Feed Back Question");
    }

    return userResponseDTO;
  }

  private boolean saveFeedBackQuestionEntity(FeedBackQuestionDTO feedBackQuestionDTO)
      throws FMSException {
    FeedBackQuestionEntity questionEntity =
        questionMapper.fromFeedBackQuestionDTO(feedBackQuestionDTO);
    return Optional.ofNullable(questionEntity)
        .map(
            userEntry -> {
              questionRepository.save(questionEntity);
              return true;
            })
        .orElseThrow(() -> new FMSException(ERROR_SAVING_DATA_TO_DB));
  }

  public List<FeedBackQuestionDTO> fetchQuestions() {
    log.debug("Fetch Feed Back Questions from DB");
    return questionMapper.fromFeedBackQuestionEntities(getFeedBackQuestions());
  }

  private List<FeedBackQuestionEntity> getFeedBackQuestions() {
    return questionRepository.findAll();
  }

  @Transactional
  public UserResponseDTO updateQuestion(FeedBackQuestionDTO feedBackQuestionDTO)
      throws FMSException {
    UserResponseDTO userResponseDTO = new UserResponseDTO();
    if (isQuestionExists(feedBackQuestionDTO.getQuestionID())) {
      FeedBackQuestionEntity questionEntity=questionRepository.findById(feedBackQuestionDTO.getQuestionID()).orElse(null);
      questionMapper.fromFeedBackQuestionDTO(feedBackQuestionDTO);
      // TO DO entity values updated by requested values

      boolean isQuestionUpdated = saveFeedBackQuestionEntity(feedBackQuestionDTO);
      if (isQuestionUpdated) {
        userResponseDTO.setSuccessMsg("Feed Back question updated successfully");
      } else {
        userResponseDTO.setErrorMsg("Error while updating Feed Back Question");
      }
      return userResponseDTO;
    } else {
      throw new FMSException(NO_DATA_IN_DB);
    }
  }

  private boolean isQuestionExists(Integer feedBackQuestionID) {
    return questionRepository.existsById(feedBackQuestionID);
  }

  @Transactional
  public void removeQuestionByID(Integer questionID) throws FMSException {
    if (isQuestionExists(questionID)) {
      questionRepository.deleteById(questionID);
    } else {
      throw new FMSException(NO_DATA_IN_DB);
    }
  }
}
