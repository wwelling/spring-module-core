package org.folio.spring.tenant.utility;

public class TenantUtility {

  public static String getSchema(String tenant, String artifact) {
    return String.format("%s_%s", tenant, artifact).replace("-", "_").toUpperCase();
  }

}
