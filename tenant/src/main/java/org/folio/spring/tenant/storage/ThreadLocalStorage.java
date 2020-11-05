package org.folio.spring.tenant.storage;

public class ThreadLocalStorage {

  private ThreadLocalStorage() {
    // do nothing
  }

  private static ThreadLocal<String> tenant = new ThreadLocal<>();

  public static void setTenant(String tenantId) {
    tenant.set(tenantId);
  }

  public static String getTenant() {
    return tenant.get();
  }

  public static void unload() {
    tenant.remove();
  }

}
