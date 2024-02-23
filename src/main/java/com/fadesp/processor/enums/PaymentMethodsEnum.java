package com.fadesp.processor.enums;

public enum PaymentMethodsEnum {

  BOLETO("BOLETO"),
  PIX("PIX"),
  CARTAO_CREDITO("CARTAO_CREDITO"),
  CARTAO_DEBITO("CARTAO_DEBITO");

  private String descricao;

  PaymentMethodsEnum(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return this.descricao;
  }
}
