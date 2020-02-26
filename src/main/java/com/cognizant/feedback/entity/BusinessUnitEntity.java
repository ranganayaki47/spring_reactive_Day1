package com.cognizant.feedback.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "businessunit")
public class BusinessUnitEntity extends AuditEntity implements Serializable {

  private static final long serialVersionUID = 2214261664975956744L;

  @Id
  @Column(name = "BuID")
  private Integer buId;

  @Column(name = "BuName")
  private String buName;
}
