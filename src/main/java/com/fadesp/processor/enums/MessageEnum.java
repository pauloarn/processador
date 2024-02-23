package com.fadesp.processor.enums;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum MessageEnum {
  ERROR_DESCONHECIDO("message.api.error.desconhecido"),
  ROTA_NAO_ENCONTRADA("message.api.rota.nao.encontrada"),
  ENDPOINT_ERROR("message.api.endpoint.error"),
  VALIDACAO_ERROR("message.api.validacao.error"),
  METHOD_NOT_SUPPORTED("message.api.method.not.supported"),
  MISSING_REQUEST_PARAMETER("message.api.missing.request.parameter"),
  REQUISICAO_CONCLUIDA("message.api.requisicao_concluida"),
  ERRO_TIMEOUT("message.api.erro.timeout");

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");
  private final String description;

  MessageEnum(String description) {
    this.description = description;
  }

  public String getMessage(String... args) {
    String msg = convertToUTF8(RESOURCE_BUNDLE.getString(this.description));
    if (msg.contains("�")) {
      msg = RESOURCE_BUNDLE.getString(this.description);
    }
    return args == null ? msg : MessageFormat.format(msg, args);
  }

  private static String convertToUTF8(String message) {
    try {
      return new String(message.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    } catch (Exception e) {
      return message;
    }
  }
}
