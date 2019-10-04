package org.folio.spring.controller.advice;

import org.folio.spring.model.response.Errors;
import org.folio.spring.utility.ErrorUtility;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DomainAdvice {

  private static final Logger logger = LoggerFactory.getLogger(DomainAdvice.class);

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public Errors handleConstraintViolationException(ConstraintViolationException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public Errors handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.buildError(exception, HttpStatus.BAD_REQUEST);
  }

}
