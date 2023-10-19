package org.folio.spring.tenant;

import jakarta.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class TenantContextHelper {

  @Value("${tenant.headerName:X-Okapi-Tenant}")
  private String tenantHeaderName;

  public abstract void addTenantToContext();

  @PreDestroy
  public void removeTenantFromContext() {
    RequestContextHolder.resetRequestAttributes();
  }

  protected void addTenantToContext(String tenant) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader(tenantHeaderName, tenant);
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }

}
