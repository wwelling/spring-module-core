package org.folio.spring.domain.controller.exception;

public class SchemaNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -3475304055299328175L;

  public SchemaNotFoundException(String message) {
    super(message);
  }

  public SchemaNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

}
