package org.folio.rest.tenant.resolver;

import org.folio.rest.tenant.annotation.TenantHeader;
import org.folio.rest.tenant.utility.AnnotationUtility;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public final class TenantHeaderResolver implements HandlerMethodArgumentResolver {

  private final String tenantHeaderName;

  public TenantHeaderResolver(String tenantHeaderName) {
    this.tenantHeaderName = tenantHeaderName;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return AnnotationUtility.findMethodAnnotation(TenantHeader.class, parameter) != null;
  }

  // @formatter:off
  @Override
  public Object resolveArgument(  
    MethodParameter parameter,
    ModelAndViewContainer mavContainer,
    NativeWebRequest webRequest,
    WebDataBinderFactory binderFactory
  ) throws Exception {
    return webRequest.getHeader(tenantHeaderName);
  }
  // @formatter:on

}