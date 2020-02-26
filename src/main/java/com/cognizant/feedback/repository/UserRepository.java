package com.cognizant.feedback.repository;

import com.cognizant.feedback.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  UserEntity findByEmailIDAndPassword(String emailID, String password);

  UserEntity findByEmployeeID(Integer employeeID);

  List<UserEntity> findAllByUserRoleDetailsRoleId(@Param("RoleID")Integer roleId);
}
