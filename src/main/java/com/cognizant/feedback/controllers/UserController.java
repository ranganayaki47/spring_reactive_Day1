package com.cognizant.feedback.controllers;

import com.cognizant.feedback.dto.UserDTO;
import com.cognizant.feedback.dto.UserRequestDTO;
import com.cognizant.feedback.dto.UserResponseDTO;
import com.cognizant.feedback.exceptions.FMSException;
import com.cognizant.feedback.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<Object> validateUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
    log.debug("User Request for login :" + userRequestDTO);
    UserResponseDTO userResponseDTO = userService.validateUser(userRequestDTO);
    return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
  }

  @PutMapping("/addpmo/{employeeID}")
  public ResponseEntity<Object> addPMO(@PathVariable Integer employeeID) throws FMSException {
    log.debug("Add PMO by EmployeeID :" + employeeID);
    UserResponseDTO userResponseDTO = userService.addPMO(employeeID);
    return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
  }

  @PutMapping("/removepmo/{employeeID}")
  public ResponseEntity<Object> removePMO(@PathVariable Integer employeeID) throws FMSException {
    log.debug("Remove PMO by EmployeeID :" + employeeID);
    UserResponseDTO userResponseDTO = userService.removePMO(employeeID);
    return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
  }

  @GetMapping("/fetchpmo")
  public ResponseEntity<List<UserDTO>> getAllPMOs() {
    log.debug("Fetch all PMOs ");
    List<UserDTO> userDTOs = userService.getAllPMOs();
    return new ResponseEntity<>(userDTOs, HttpStatus.OK);
  }
}
