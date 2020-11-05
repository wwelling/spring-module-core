package org.folio.spring.tenant.storage;

public class ThreadLocalStorage {

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
