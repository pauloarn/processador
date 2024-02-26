package com.fadesp.processor.service.DTO;

import com.fadesp.processor.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.TypedQuery;

@Getter
@Setter
@AllArgsConstructor
public class PaymentCriteriaDTO {
  private Long totalPayments;
  private TypedQuery<Payment> paymentQuery;
}
