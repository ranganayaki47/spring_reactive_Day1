package com.cognizant.feedback.mapper;

import com.cognizant.feedback.dto.FeedBackQuestionDTO;
import com.cognizant.feedback.entity.FeedBackQuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FeedBackQuestionMapper {
  @Mapping(target="totalNoOfAnswers",expression="java(feedBackQuestionDTO.getAnswers().size())")
  FeedBackQuestionEntity fromFeedBackQuestionDTO(FeedBackQuestionDTO feedBackQuestionDTO);

  FeedBackQuestionDTO fromFeedBackQuestionEntity(FeedBackQuestionEntity questionEntity);

  List<FeedBackQuestionDTO> fromFeedBackQuestionEntities(
      List<FeedBackQuestionEntity> questionEntities);
}
