package com.cognizant.feedback.repository;

import com.cognizant.feedback.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {

  UserRoleEntity findByRoleName(String roleName);
}
