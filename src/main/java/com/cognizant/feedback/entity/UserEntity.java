package com.cognizant.feedback.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user")
public class UserEntity extends AuditEntity implements Serializable {

  private static final long serialVersionUID = 6314020964436269577L;

  @Id
  @Column(name = "EmployeeID", unique = true)
  private Integer employeeID;

  @Column(name = "EmailID", unique = true)
  private String emailID;

  @Column(name = "Password")
  private String password;

  @Column(name = "UserName")
  private String userName;

  @Column(name = "FirstName")
  private String firstName;

  @Column(name = "LastName")
  private String lastName;

  @Column(name = "ContactNo")
  private Long contactNo;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(referencedColumnName = "RoleID",name="RoleID")
  private UserRoleEntity userRoleDetails;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "BuID")
  private BusinessUnitEntity businessUnit;
}
