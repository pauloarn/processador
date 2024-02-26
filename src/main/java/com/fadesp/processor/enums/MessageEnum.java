package com.fadesp.processor.enums;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public enum MessageEnum {
  UNKNOWN_ERROR("message.api.unknown.error"),
  AMOUNT_PAID_SHOULD_NOT_BE_NEGATIVE("message.api.amoun.paid.should.not.be.negative"),
  PAYMENT_FINALIZED("message.api.payment.finalized"),
  DEBIT_ALREADY_REGISTERED("message.api.debit.already.registered"),
  CPF_CNPJ_INCORRECT_SIZE("message.api.cpf.cnpj.incorrect.size"),
  CPF_CNPJ_NEEDED("message.api.cpf.cnpj.needed"),
  PAYMENT_ALREADY_PROCESSED("message.api.payment.already.processed"),
  NEED_CARD_NUMBER_FOR_CARD_PAYMENT("message.api.need.card.number.for.card.payment"),
  PAYMENT_NOT_FOUND("message.api.payment.not.found"),
  ROUTE_NOT_FOUND("message.api.route.not.found"),
  ENDPOINT_ERROR("message.api.endpoint.error"),
  VALIDATION_ERROR("message.api.validation.error"),
  METHOD_NOT_SUPPORTED("message.api.method.not.supported"),
  MISSING_REQUEST_PARAMETER("message.api.missing.request.parameter"),
  REQUEST_FINISHED("message.api.request.finished"),
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
