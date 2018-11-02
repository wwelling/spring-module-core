package org.folio.rest.tenant.controller.advice;

import java.io.IOException;
import java.sql.SQLException;

import org.folio.rest.model.response.Errors;
import org.folio.rest.tenant.exception.TenantAlreadyExistsException;
import org.folio.rest.tenant.exception.TenantDoesNotExistsException;
import org.folio.rest.utility.ErrorUtility;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TenantControllerAdvice {

  private static final Logger logger = LoggerFactory.getLogger(TenantControllerAdvice.class);

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = SQLException.class)
  public Errors hasndleSQLException(SQLException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.craftErrors(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = IOException.class)
  public Errors hasndleIOException(IOException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.craftErrors(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = HibernateException.class)
  public Errors hasndleHibernateException(HibernateException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.craftErrors(exception, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ExceptionHandler(value = TenantAlreadyExistsException.class)
  public Errors hasndleTenantAlreadyExistsException(TenantAlreadyExistsException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.craftErrors(exception, HttpStatus.NO_CONTENT);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = TenantDoesNotExistsException.class)
  public Errors hasndleTenantDoesNotExistsException(TenantDoesNotExistsException exception) {
    logger.debug(exception.getMessage(), exception);
    return ErrorUtility.craftErrors(exception, HttpStatus.BAD_REQUEST);
  }

}
