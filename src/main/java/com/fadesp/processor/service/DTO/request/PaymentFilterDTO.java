package com.fadesp.processor.service.DTO.request;

import com.fadesp.processor.enums.PaymentStatusEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentFilterDTO {
  private Integer paymentCode;
  private String cpfCnpj;
  private PaymentStatusEnum paymentStatus;
  private Integer page;
  private Integer size;
}
