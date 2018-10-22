package org.folio.rest.controller.advice;

import java.io.IOException;

import org.folio.rest.controller.exception.SchemaNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SchemaAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(SchemaNotFoundException.class)
  public String handleSchemaNotFoundException(SchemaNotFoundException exception) {
    return exception.getMessage();
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(IOException.class)
  public String handleIOException(IOException exception) {
    return exception.getMessage();
  }

}
