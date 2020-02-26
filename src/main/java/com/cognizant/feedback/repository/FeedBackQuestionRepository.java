package com.cognizant.feedback.repository;

import com.cognizant.feedback.entity.FeedBackQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackQuestionRepository extends JpaRepository<FeedBackQuestionEntity,Integer> {


}
