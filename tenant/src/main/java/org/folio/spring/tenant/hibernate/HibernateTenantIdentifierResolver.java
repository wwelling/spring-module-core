package org.folio.spring.tenant.hibernate;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.folio.spring.tenant.config.TenantConfig;
import org.folio.spring.tenant.exception.NoTenantHeaderException;
import org.folio.spring.tenant.storage.ThreadLocalStorage;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@DependsOn("hibernateSchemaService")
public class HibernateTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

  @Autowired
  private TenantConfig tenantConfig;

  @Override
  public String resolveCurrentTenantIdentifier() {
    Optional<HttpServletRequest> request = getRequestFromContext();
    if (request.isPresent()) {
      String tenant = request.get().getHeader(tenantConfig.getHeaderName());
      if (tenant != null) {
        return tenant;
      }
      if (tenantConfig.isForceTenant()) {
        throw new NoTenantHeaderException("No tenant header on request!");
      }
    } else {
      String tenant = ThreadLocalStorage.getTenant();
      if (tenant != null) {
        return tenant;
      }
      // NOTE: not enforcing tenant here
    }
    return tenantConfig.getDefaultTenant();
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }

  private Optional<HttpServletRequest> getRequestFromContext() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes != null) {
      return Optional.ofNullable(((ServletRequestAttributes) requestAttributes).getRequest());
    }
    return Optional.empty();
  }

}
