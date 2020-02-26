package com.cognizant.feedback.controllers;

import com.cognizant.feedback.service.FileWatcherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class FileWatcherController {
  private final FileWatcherService fileWatcherService;

  @PostMapping("/processevents")
  public void processEventExcelFiles() {
    fileWatcherService.processFiles();
  }
}
