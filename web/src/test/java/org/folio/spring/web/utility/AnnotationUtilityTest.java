package org.folio.spring.web.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.request.NativeWebRequest;

@ExtendWith(MockitoExtension.class)
class AnnotationUtilityTest {

  @Mock
  private MethodParameter parameter;

  @Mock
  private NativeWebRequest nativeWebRequest;

  FakeAnnotation fakeAnnotation;

  @BeforeEach
  void beforeEach() {
    fakeAnnotation = new FakeAnnotation();
  }

  @Test
  void supportsParameterReturnsNullTest() {
    Annotation[] annotations = new Annotation[0];

    when(parameter.getParameterAnnotation(any())).thenReturn((Annotation) null);
    when(parameter.getParameterAnnotations()).thenReturn(annotations);

    Annotation result = AnnotationUtility.findMethodAnnotation(FakeAnnotation.class, parameter);

    assertNull(result);
  }

  @Test
  void supportsParameterReturnsAnnotationForClassTest() {
    when(parameter.getParameterAnnotation(any())).thenReturn(fakeAnnotation);

    Annotation result = AnnotationUtility.findMethodAnnotation(FakeAnnotation.class, parameter);

    assertEquals(fakeAnnotation, result);
  }

  @Test
  void supportsParameterReturnsNullForListTest() {
    Annotation[] annotations = { fakeAnnotation };

    when(parameter.getParameterAnnotation(any())).thenReturn((Annotation) null);
    when(parameter.getParameterAnnotations()).thenReturn(annotations);

    Annotation result = AnnotationUtility.findMethodAnnotation(OtherFakeAnnotation.class, parameter);

    assertNull(result);
  }

  @Test
  void supportsParameterReturnsAnnotationForListTest() {
    Annotation[] annotations = { fakeAnnotation };

    when(parameter.getParameterAnnotation(any())).thenReturn((Annotation) null);
    when(parameter.getParameterAnnotations()).thenReturn(annotations);

    try (MockedStatic<AnnotationUtils> utility = Mockito.mockStatic(AnnotationUtils.class)) {
      utility.when(() -> AnnotationUtils.findAnnotation((Class<?>) any(Class.class), any())).thenReturn(fakeAnnotation);

      Annotation result = AnnotationUtility.findMethodAnnotation(FakeAnnotation.class, parameter);

      assertEquals(fakeAnnotation, result);
    }
  }

  private class FakeAnnotation implements Annotation {
    @Override
    public Class<? extends Annotation> annotationType() {
      return FakeAnnotation.class;
  }};

  private class OtherFakeAnnotation implements Annotation {
    @Override
    public Class<? extends Annotation> annotationType() {
      return OtherFakeAnnotation.class;
  }};

}
