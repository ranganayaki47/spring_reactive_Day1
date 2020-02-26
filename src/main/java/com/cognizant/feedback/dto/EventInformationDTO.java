package com.cognizant.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventInformationDTO implements Serializable {

    private static final long serialVersionUID = -5859133284043827489L;

    private String eventName;
    private String eventDescription;
    private LocalDate eventDate;
    private Integer employeeID;
    private String employeeName;
    private Integer volunteerHours;
    private Integer travelHours;
    private Integer livesImpacted;
    private String businessUnit;
    private String status;
    private  String iiepCategory;

}
