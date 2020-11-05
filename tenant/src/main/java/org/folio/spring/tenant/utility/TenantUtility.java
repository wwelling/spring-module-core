package org.folio.spring.tenant.utility;

public class TenantUtility {

  private TenantUtility() {
    // do nothing
  }

  public static String getSchema(String tenant, String artifact) {
    return String.format("%s_%s", tenant, artifact).replace("-", "_").toUpperCase();
  }

}
