package com.fadesp.processor.service.DTO.response;

import com.fadesp.processor.enums.PaymentMethodsEnum;
import com.fadesp.processor.enums.PaymentStatusEnum;
import com.fadesp.processor.model.Payment;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PaymentDTO {
  private Integer idPagamento;
  private Integer codigoDebito;
  private PaymentStatusEnum statusPagamento;
  private String cpfCnpj;
  private PaymentMethodsEnum metodoPagamento;
  private String numeroCartao;
  private BigDecimal valorPagamento;

  public PaymentDTO(Payment payment) {
    this.setStatusPagamento(payment.getPaymentStatus());
    this.setIdPagamento(payment.getPaymentId());
    this.setValorPagamento(payment.getPaymentAmount());
    this.setCodigoDebito(payment.getPaymentCode());
    this.setCpfCnpj(payment.getCpfCnpj());
    this.setNumeroCartao(payment.getCardNumber());
    this.setMetodoPagamento(payment.getPaymentMethod());
  }

  public static List<PaymentDTO> toList(List<Payment> payments) {
    return payments.stream().map(PaymentDTO::new).collect(Collectors.toList());
  }
}
