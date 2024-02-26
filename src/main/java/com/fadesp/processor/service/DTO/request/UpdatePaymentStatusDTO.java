package com.fadesp.processor.service.DTO.request;

import com.fadesp.processor.enums.PaymentStatusEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentStatusDTO {
  private PaymentStatusEnum statusPagamento;
}
