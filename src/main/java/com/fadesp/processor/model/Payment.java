package com.fadesp.processor.model;

import com.fadesp.processor.config.Catalog;
import com.fadesp.processor.config.Schema;
import com.fadesp.processor.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="payment", catalog = Catalog.FADESP,schema = Schema.PROCESSOR)
public class Payment extends BaseEntity {
  @Id
  @Column(name="id")
  private Long paymentId;
}
