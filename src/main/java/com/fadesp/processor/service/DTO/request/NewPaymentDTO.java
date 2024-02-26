package com.fadesp.processor.service.DTO.request;

import com.fadesp.processor.enums.PaymentMethodsEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class NewPaymentDTO {
  @NotNull
  private Integer codigoDebito;
  @NotNull
  private String cpfCnpj;
  @NotNull
  private PaymentMethodsEnum metodoPagamento;
  private String numeroCartao;
  @NotNull
  private BigDecimal valorPagamento;
}
