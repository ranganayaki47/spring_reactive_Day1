package com.cognizant.feedback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static com.cognizant.feedback.constants.FileConstants.PACKAGE_CONSTANT;

@SpringBootApplication
public class FeedbackApplication {
  private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(FeedbackApplication.class, args);
    LOGGER.info("!--- Feedback Management Application Started ---!");
  }
}
