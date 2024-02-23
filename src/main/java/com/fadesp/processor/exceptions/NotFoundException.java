package com.fadesp.processor.exceptions;

import com.fadesp.processor.enums.MessageEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class NotFoundException extends ApiErrorException {
  public NotFoundException(MessageEnum messageEnum) {
    super(HttpStatus.NOT_FOUND, messageEnum);
  }
}
