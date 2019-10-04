package org.folio.spring.tenant.exception;

public class NoTenantHeaderException extends RuntimeException {

  private static final long serialVersionUID = -6276062640659833897L;

  public NoTenantHeaderException(String message) {
    super(message);
  }

}
