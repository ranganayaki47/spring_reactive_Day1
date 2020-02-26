package com.cognizant.feedback.entity;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class AuditEntity implements Serializable {

  private static final long serialVersionUID = 4508919620192218742L;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIMESTAMP")
  private java.util.Date createdTimestamp;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIMESTAMP")
  private java.util.Date updatedTimestamp;

  @Column(name = "CREATED_USERID")
  private String createUserID;

  @Column(name = "CREATED_PROGRAM_NAME")
  private String createProgramName;

  @Column(name = "UPDATED_USERID")
  private String lastUpdateUserID;

  @Column(name = "UPDATED_PROGRAM_NAME")
  private String lastUpdateProgramName;

  @PrePersist
  private void prePersistAudit() {
    createUserID = "admin";
    createProgramName = "OutreachFeedback";
    lastUpdateUserID = "admin";
    lastUpdateProgramName = "OutreachFeedback";
    createdTimestamp = new java.util.Date();
    updatedTimestamp = new java.util.Date();
  }

  @PreUpdate
  public void preUpdateAudit() {
    updatedTimestamp = new java.util.Date();
  }

  public java.util.Date getCreatedTimestamp() {
    return createdTimestamp;
  }

  public void setCreatedTimestamp(java.util.Date createdTimestamp) {
    this.createdTimestamp = createdTimestamp;
  }

  public java.util.Date getUpdatedTimestamp() {
    return updatedTimestamp;
  }

  public void setUpdatedTimestamp(java.util.Date updatedTimestamp) {
    this.updatedTimestamp = updatedTimestamp;
  }

  public String getCreateUserID() {
    return createUserID;
  }

  public void setCreateUserID(String createUserID) {
    this.createUserID = createUserID;
  }

  public String getCreateProgramName() {
    return createProgramName;
  }

  public void setCreateProgramName(String createProgramName) {
    this.createProgramName = createProgramName;
  }

  public String getLastUpdateUserID() {
    return lastUpdateUserID;
  }

  public void setLastUpdateUserID(String lastUpdateUserID) {
    this.lastUpdateUserID = lastUpdateUserID;
  }

  public String getLastUpdateProgramName() {
    return lastUpdateProgramName;
  }

  public void setLastUpdateProgramName(String lastUpdateProgramName) {
    this.lastUpdateProgramName = lastUpdateProgramName;
  }
}
