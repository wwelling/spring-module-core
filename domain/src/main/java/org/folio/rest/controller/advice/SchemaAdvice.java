package org.folio.rest.controller.advice;

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

}
