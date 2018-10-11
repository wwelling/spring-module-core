package org.folio.rest.tenant.controller.advice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.io.IOException;
import java.sql.SQLException;

import org.folio.rest.tenant.exception.TenantAlreadyExistsException;
import org.folio.rest.tenant.exception.TenantDoesNotExistsException;
import org.hibernate.HibernateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class TenantControllerAdvice {

  @ExceptionHandler(value = SQLException.class)
  public ResponseEntity<String> hasndleSQLException(SQLException exception) {
    return new ResponseEntity<String>(exception.getMessage(), INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = IOException.class)
  public ResponseEntity<String> hasndleIOException(IOException exception) {
    return new ResponseEntity<String>(exception.getMessage(), INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = HibernateException.class)
  public ResponseEntity<String> hasndleHibernateException(HibernateException exception) {
    return new ResponseEntity<String>(exception.getMessage(), INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = TenantAlreadyExistsException.class)
  public ResponseEntity<String> hasndleTenantAlreadyExistsException(TenantAlreadyExistsException exception) {
    return new ResponseEntity<String>(exception.getMessage(), NO_CONTENT);
  }

  @ExceptionHandler(value = TenantDoesNotExistsException.class)
  public ResponseEntity<String> hasndleTenantDoesNotExistsException(TenantDoesNotExistsException exception) {
    return new ResponseEntity<String>(exception.getMessage(), BAD_REQUEST);
  }

}
