package com.cognizant.feedback.service;

import com.cognizant.feedback.dto.UserDTO;
import com.cognizant.feedback.dto.UserRequestDTO;
import com.cognizant.feedback.dto.UserResponseDTO;
import com.cognizant.feedback.entity.UserEntity;
import com.cognizant.feedback.entity.UserRoleEntity;
import com.cognizant.feedback.exceptions.FMSException;
import com.cognizant.feedback.mapper.UserMapper;
import com.cognizant.feedback.repository.UserRepository;
import com.cognizant.feedback.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.cognizant.feedback.constants.ErrorConstants.NO_DATA_IN_DB;
import static com.cognizant.feedback.constants.UserConstants.*;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;
  private final UserMapper userMapper;

  public UserResponseDTO validateUser(UserRequestDTO userRequestDTO) {
    return Optional.ofNullable(userRequestDTO)
        .filter(userRequest -> !userRequest.getEmailID().isEmpty())
        .map(this::isValidUser)
        .orElseGet(this::getUserErrorDTO);
  }

  private UserResponseDTO getUserErrorDTO() {
    UserResponseDTO userResponseDTO = new UserResponseDTO();
    userResponseDTO.setErrorMsg("Invalid Username or Password.. Try again");
    return userResponseDTO;
  }

  private UserResponseDTO getUserSuccessDTO(UserRoleEntity userRoleEntity) {
    UserResponseDTO userResponseDTO = new UserResponseDTO();
    userResponseDTO.setSuccessMsg("Successfully logged into FeedBack Application");
    userResponseDTO.setUserRole(userRoleEntity.getRoleName());
    return userResponseDTO;
  }

  private UserEntity findUserByEmailID(UserRequestDTO userReq) {
    return userRepository.findByEmailIDAndPassword(userReq.getEmailID(), userReq.getPassword());
  }

  private UserResponseDTO isValidUser(UserRequestDTO userReq) {
    UserEntity userEntity = findUserByEmailID(userReq);
    return Optional.ofNullable(userEntity.getUserRoleDetails())
        .filter(userAdmin -> userAdmin.getRoleName().equals(ADMIN))
        .map(this::getUserSuccessDTO)
        .orElseGet(
            () ->
                Optional.ofNullable(userEntity.getUserRoleDetails())
                    .filter(userPMO -> userPMO.getRoleName().equals(PMO))
                    .map(this::getUserSuccessDTO)
                    .orElseGet(
                        () ->
                            Optional.ofNullable(userEntity.getUserRoleDetails())
                                .filter(userPOC -> userPOC.getRoleName().equals(POC))
                                .map(this::getUserSuccessDTO)
                                .orElse(getUserErrorDTO())));
  }

  @Transactional
  public UserResponseDTO addPMO(Integer employeeID) throws FMSException {
    UserResponseDTO userResponseDTO = new UserResponseDTO();
    boolean isPMOAdded = addRemovePMOFromDB(employeeID, PMO);
    if (isPMOAdded) {
      userResponseDTO.setSuccessMsg("PMO added successfully");
    } else {
      userResponseDTO.setErrorMsg("Error adding PMO.. Try again");
    }

    return userResponseDTO;
  }

  @Transactional
  public UserResponseDTO removePMO(Integer employeeID) throws FMSException {
    UserResponseDTO userResponseDTO = new UserResponseDTO();
    boolean isPMORemoved = addRemovePMOFromDB(employeeID, "");
    if (isPMORemoved) {
      userResponseDTO.setSuccessMsg("PMO removed successfully");
    } else {
      userResponseDTO.setErrorMsg("Error removing PMO.. Try again");
    }
    return userResponseDTO;
  }

  private UserEntity checkUserEntry(Integer employeeID) {
    return userRepository.findByEmployeeID(employeeID);
  }

  private boolean addRemovePMOFromDB(Integer employeeID, String roleName) throws FMSException {
    UserEntity userEntity = checkUserEntry(employeeID);
    return Optional.ofNullable(userEntity)
        .map(
            userEntry -> {
              userEntity.setUserRoleDetails(getUserRole(roleName));
              userRepository.save(userEntity);
              return true;
            })
        .orElseThrow(() -> new FMSException(NO_DATA_IN_DB));
  }

  private UserRoleEntity getUserRole(String roleName) {
    return userRoleRepository.findByRoleName(roleName);
  }

  public List<UserDTO> getAllPMOs() {
    UserRoleEntity userRoleEntity = getUserRole(PMO);
    List<UserEntity> userEntities =
        userRepository.findAllByUserRoleDetailsRoleId(userRoleEntity.getRoleId());
    List<UserDTO> userDTOS = userMapper.userEntitiesToUserDTOs(userEntities);
    return userDTOS;
  }
}
