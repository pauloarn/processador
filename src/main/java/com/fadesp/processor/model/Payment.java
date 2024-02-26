package com.fadesp.processor.model;

import com.fadesp.processor.config.Catalog;
import com.fadesp.processor.config.Schema;
import com.fadesp.processor.enums.PaymentMethodsEnum;
import com.fadesp.processor.enums.PaymentStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "payment", catalog = Catalog.FADESP, schema = Schema.PROCESSOR)
public class Payment extends BaseEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer paymentId;

  @Column(name = "paymentCode")
  private Integer paymentCode;

  @Column(name = "cpfCnpj")
  private String cpfCnpj;

  @Column(name = "paymentMethod")
  @Enumerated(EnumType.STRING)
  private PaymentMethodsEnum paymentMethod;

  @Column(name = "cardNumber")
  private String cardNumber;

  @Column(name = "paymentAmount")
  private BigDecimal paymentAmount;

  @Column(name = "paymentStatus")
  @Enumerated(EnumType.STRING)
  private PaymentStatusEnum paymentStatus;

}
