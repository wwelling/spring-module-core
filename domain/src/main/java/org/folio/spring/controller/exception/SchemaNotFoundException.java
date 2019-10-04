package org.folio.spring.controller.exception;

public class SchemaNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -3475304055299328175L;

  public SchemaNotFoundException(String message) {
    super(message);
  }

}
