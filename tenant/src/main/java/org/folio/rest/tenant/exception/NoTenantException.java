package org.folio.rest.tenant.exception;

public class NoTenantException extends RuntimeException {

  private static final long serialVersionUID = -9077629270135462064L;

  public NoTenantException(String message) {
    super(message);
  }

}
