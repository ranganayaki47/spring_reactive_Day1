package com.cognizant.feedback.controllers;

import com.cognizant.feedback.dto.EventInformationDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class FeedbackManagementController {

    @PostMapping("/eventsummarydetails")
    public ResponseEntity<EventInformationDTO> addEquipmentGroup() {

        return null;
    }
}
