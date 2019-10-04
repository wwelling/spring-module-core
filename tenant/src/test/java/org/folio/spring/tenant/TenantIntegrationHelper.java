package org.folio.spring.tenant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

public abstract class TenantIntegrationHelper {

  @Value("${tenant.headerName:X-Okapi-Tenant}")
  private String tenantHeaderName;

  protected MockHttpServletRequestBuilder tenantGet(String tenant, String path) {
    return get(path).header(tenantHeaderName, tenant);
  }

  protected MockHttpServletRequestBuilder tenantPost(String tenant, String path) {
    return post(path).header(tenantHeaderName, tenant);
  }

  protected MockHttpServletRequestBuilder tenantPut(String tenant, String path) {
    return put(path).header(tenantHeaderName, tenant);
  }

  protected MockHttpServletRequestBuilder tenantPatch(String tenant, String path) {
    return patch(path).header(tenantHeaderName, tenant);
  }

  protected MockHttpServletRequestBuilder tenantDelete(String tenant, String path) {
    return delete(path).header(tenantHeaderName, tenant);
  }

}
