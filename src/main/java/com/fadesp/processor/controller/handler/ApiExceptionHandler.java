package com.fadesp.processor.controller.handler;

import com.fadesp.processor.enums.MessageEnum;
import com.fadesp.processor.exceptions.ApiErrorException;
import com.fadesp.processor.service.DTO.Response;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fadesp.processor.utils.ExceptionUtils.logException;
import static java.lang.String.format;
import static java.lang.String.join;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Log4j2
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException exception,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request
  ) {
    Response<String> response = new Response<>();
    log.error("Rota não encontrada");
    response.setStatusCode(status, MessageEnum.ROUTE_NOT_FOUND);
    response.setData(format("Rota %s não encontrada!", request.getDescription(false).substring(4)));
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request
  ) {
    logException(ex);
    Response<String> response = new Response<>();
    response.setStatusCode(status, MessageEnum.MISSING_REQUEST_PARAMETER);
    response.setData(format("Parâmetro obrigatório: %s", ex.getParameterName()));
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request
  ) {
    logException(ex);
    Response<String> response = new Response<>();
    response.setStatusCode(status, MessageEnum.ENDPOINT_ERROR);
    response.setData(format("Error na conversão do parâmetro [ %s ]: %s", ex.getPropertyName(), ex.getMessage()));
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request
  ) {
    Response<String> response = new Response<>();
    response.setStatusCode(status, MessageEnum.VALIDATION_ERROR);

    if (ex.getCause() instanceof InvalidFormatException) {
      var inEx = (InvalidFormatException) ex.getCause();
      var pathBuild = new ArrayList<String>();
      inEx.getPath().forEach(p -> {
        if (p.getIndex() > -1) {
          pathBuild.add(String.valueOf(p.getIndex()));
        } else {
          pathBuild.add(p.getFieldName());
        }
      });
      var path = join(".", pathBuild);
      var msg = String.format("Error na conversão do campo [ %s ] de valor [ %s ] para %s", path, inEx.getValue(), inEx.getTargetType().getSimpleName());
      response.setData(msg);
    } else {
      response.setData(ex.getMessage());
    }

    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request
  ) {
    log.error("Ocorreu um error de validação");
    logException(ex);
    Response<Map<String, String>> response = new Response<>();
    Map<String, String> errors = new HashMap<>();
    response.setStatusCode(status, MessageEnum.VALIDATION_ERROR);
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    response.setData(errors);
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request
  ) {
    StringBuilder builder = new StringBuilder();
    builder.append(ex.getMethod());
    builder.append(
        " não suportado para está rota. Métodos suportados são:  ");
    Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(", "));

    Response<String> response = new Response<>();
    response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED, MessageEnum.METHOD_NOT_SUPPORTED);
    response.setData(builder.substring(0, builder.length() - 2));
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @ExceptionHandler(ApiErrorException.class)
  public ResponseEntity<Object> apiErrorException(ApiErrorException exception) {
    log.error("Um error foi disparado!!!");

    Response<String> response = new Response<>();
    response.setStatusCode(exception.getHttpStatus(), exception.getMessageApiEnum(), exception.getArgs());
    response.setData(exception.getErrorMsg());
    log.error("[ {} ] :: {}", exception.getMessageApiEnum(), response.getMessage());

    if (exception.getEx() != null) logException(exception.getEx());

    return ResponseEntity.status(response.getStatusCode()).body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> allException(Exception exception) {
    Response<String> response = new Response<>();
    log.error("Um error inesperado ocorreu!!!");
    logException(exception);
    response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR, MessageEnum.UNKNOWN_ERROR);
    response.setData(exception.getMessage());
    return ResponseEntity.status(response.getStatusCode()).body(response);
  }
}
