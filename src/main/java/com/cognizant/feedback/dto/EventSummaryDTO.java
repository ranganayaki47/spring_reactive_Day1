package com.cognizant.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryDTO implements Serializable {

    private static final long serialVersionUID = -6207993132956938993L;

    private String eventID;
    private String month;
    private String baseLocation;
    private String beneficiaryName;
    private String venueAddress;
    private String councilName;
    private String project;
    private String category;
    private String eventName;
    private String eventDescription;
    private LocalDate eventDate;
    private Integer totalNoOfVolunteers;
    private Integer totalVolunteerHours;
    private Integer totalTravelHours;
    private Integer overallVolunteerHours;
    private Integer livesImpacted;
    private Integer activityType;
    private String status;
    private Integer pocID;
    private String pocName;
    private Integer pocContactNumber;
    
}
