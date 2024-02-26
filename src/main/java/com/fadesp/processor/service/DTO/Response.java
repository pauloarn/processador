package com.fadesp.processor.service.DTO;


import com.fadesp.processor.enums.MessageEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
public class Response<D> implements Serializable {
  private Integer statusCode = 400;
  private String messageCode;
  private String message;
  D body;

  public Response<D> setStatusCode(HttpStatus httpStatus, String messageCode, String message) {
    this.statusCode = httpStatus.value();
    this.messageCode = messageCode;
    this.message = message;
    return this;
  }

  public Response<D> setStatusCode(HttpStatus httpStatus, MessageEnum messageApiEnum, String... args) {
    this.statusCode = httpStatus.value();
    this.messageCode = messageApiEnum.name();
    this.message = messageApiEnum.getMessage(args);
    return this;
  }

  public void setBadRequest(MessageEnum messageApiEnum) {
    this.statusCode = HttpStatus.BAD_REQUEST.value();
    this.messageCode = messageApiEnum.name();
    this.message = messageApiEnum.getMessage();
  }

  public Response<D> setOk(MessageEnum messageApiEnum) {
    this.statusCode = 200;
    this.messageCode = messageApiEnum.name();
    this.message = messageApiEnum.getMessage();
    return this;
  }

  public Response<D> setOk() {
    MessageEnum messageApiEnum = MessageEnum.REQUEST_FINISHED;
    this.statusCode = 200;
    this.messageCode = messageApiEnum.name();
    this.message = messageApiEnum.getMessage();
    return this;
  }

  public Response<D> setCreated() {
    MessageEnum messageApiEnum = MessageEnum.REQUEST_FINISHED;
    this.statusCode = 201;
    this.messageCode = messageApiEnum.name();
    this.message = messageApiEnum.getMessage();
    return this;
  }

  public void setData(D data) {
    this.body = data;
  }
}
