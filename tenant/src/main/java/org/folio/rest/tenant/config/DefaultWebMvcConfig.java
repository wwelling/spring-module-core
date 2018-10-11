package org.folio.rest.tenant.config;

import java.util.List;

import org.folio.rest.tenant.resolver.TenantHeaderResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DefaultWebMvcConfig implements WebMvcConfigurer {

  @Autowired
  private TenantConfig tenantConfig;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    argumentResolvers.add(new TenantHeaderResolver(tenantConfig.getHeaderName()));
  }

}
