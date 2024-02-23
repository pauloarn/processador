package com.fadesp.processor.exceptions;

import com.fadesp.processor.enums.MessageEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@NoArgsConstructor
public class BadRequestException extends ApiErrorException {

  public BadRequestException(MessageEnum messageEnum) {
    super(HttpStatus.BAD_REQUEST, messageEnum);
  }

  public BadRequestException(MessageEnum messageEnum, List<String> args) {
    this.httpStatus = HttpStatus.BAD_REQUEST;
    this.messageApiEnum = messageEnum;
    this.args = args.toArray(String[]::new);
  }
}
