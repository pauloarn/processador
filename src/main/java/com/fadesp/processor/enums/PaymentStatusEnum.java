package com.fadesp.processor.enums;

public enum PaymentStatusEnum {

  PENDENTE_PROCESSAMENTO("PENDENTE_PROCESSAMENTO"),
  PROCESSADO_COM_SUCESSO("PROCESSADO_COM_SUCESSO"),
  PROCESSADO_COM_FALHA("PROCESSADO_COM_FALHA");
  private String descricao;

  PaymentStatusEnum(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return this.descricao;
  }
}
