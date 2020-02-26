package com.cognizant.feedback.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "userrole")
public class UserRoleEntity extends AuditEntity implements Serializable {

  private static final long serialVersionUID = 759790030008440167L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "RoleID")
  private Integer roleId;

  @Column(name = "RoleName")
  private String roleName;
}
