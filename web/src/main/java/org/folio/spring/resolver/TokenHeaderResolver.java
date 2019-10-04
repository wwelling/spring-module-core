package org.folio.spring.resolver;

import org.folio.spring.annotation.TokenHeader;
import org.folio.spring.utility.AnnotationUtility;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public final class TokenHeaderResolver implements HandlerMethodArgumentResolver {

  private final String tenantHeaderName;

  public TokenHeaderResolver(String tenantHeaderName) {
    this.tenantHeaderName = tenantHeaderName;
  }

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return AnnotationUtility.findMethodAnnotation(TokenHeader.class, parameter) != null;
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
