package org.folio.spring.domain.controller.exception;

public class SchemaIOException extends RuntimeException {

  private static final long serialVersionUID = -8336467594353513798L;

  public SchemaIOException(String message, Throwable cause) {
    super(message, cause);
  }

}
