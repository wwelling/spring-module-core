package org.folio.spring.web.resolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import org.folio.spring.web.utility.AnnotationUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.NativeWebRequest;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class TokenHeaderResolverTest {

  @Mock
  private MethodParameter parameter;

  @Mock
  private NativeWebRequest nativeWebRequest;

  private TokenHeaderResolver tokenHeaderResolver;

  @BeforeEach
  void beforeEach() {
    tokenHeaderResolver = new TokenHeaderResolver("TokenHeaderResolver");
  }

  @Test
  void supportsParameterReturnsFalseTest() {
    try (MockedStatic<AnnotationUtility> utility = Mockito.mockStatic(AnnotationUtility.class)) {
      utility.when(() -> AnnotationUtility.findMethodAnnotation(any(), any())).thenReturn(null);

      boolean result = tokenHeaderResolver.supportsParameter(parameter);

      assertEquals(false, result);
    }
  }

  @Test
  void supportsParameterReturnsTrueTest() {
    try (MockedStatic<AnnotationUtility> utility = Mockito.mockStatic(AnnotationUtility.class)) {
      utility.when(() -> AnnotationUtility.findMethodAnnotation(any(), any())).thenReturn(new FakeAnnotation());

      boolean result = tokenHeaderResolver.supportsParameter(parameter);

      assertEquals(true, result);
    }
  }

  @Test
  void resolveArgumentWorksTest() throws Exception {
    String works = "works";

    when(nativeWebRequest.getHeader(anyString())).thenReturn(works);

    Object result = tokenHeaderResolver.resolveArgument(null, null, nativeWebRequest, null);

    assertEquals(works, result);
  }

  private class FakeAnnotation implements Annotation {
    @Override
    public Class<? extends Annotation> annotationType() {
      return FakeAnnotation.class;
  }};

}
