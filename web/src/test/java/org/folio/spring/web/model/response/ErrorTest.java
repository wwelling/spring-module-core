package org.folio.spring.web.model.response;

import static org.folio.spring.test.mock.MockMvcConstant.KEY;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ErrorTest {

  @InjectMocks
  private Error error;

  @Test
  void getMessageWorksTest() {
    setField(error, "message", VALUE);
    assertEquals(VALUE, error.getMessage());
  }

  @Test
  void setMessageWorksTest() {
    setField(error, "message", KEY);

    error.setMessage(VALUE);
    assertEquals(VALUE, getField(error, "message"));
  }

  @Test
  void withMessageWorksTest() {
    setField(error, "message", KEY);

    Error newError = error.withMessage(VALUE);
    assertEquals(VALUE, getField(error, "message"));
    assertEquals(VALUE, getField(newError, "message"));
  }

  @Test
  void getTypeWorksTest() {
    setField(error, "type", VALUE);
    assertEquals(VALUE, error.getType());
  }

  @Test
  void setTypeWorksTest() {
    setField(error, "type", KEY);

    error.setType(VALUE);
    assertEquals(VALUE, getField(error, "type"));
  }

  @Test
  void withTypeWorksTest() {
    setField(error, "type", KEY);

    Error newError = error.withType(VALUE);
    assertEquals(VALUE, getField(error, "type"));
    assertEquals(VALUE, getField(newError, "type"));
  }

  @Test
  void getCodeWorksTest() {
    setField(error, "code", VALUE);
    assertEquals(VALUE, error.getCode());
  }

  @Test
  void setCodeWorksTest() {
    setField(error, "code", KEY);

    error.setCode(VALUE);
    assertEquals(VALUE, getField(error, "code"));
  }

  @Test
  void withCodeWorksTest() {
    setField(error, "code", KEY);

    Error newError = error.withCode(VALUE);
    assertEquals(VALUE, getField(error, "code"));
    assertEquals(VALUE, getField(newError, "code"));
  }

  @Test
  void getParametersWorksTest() {
    Parameter parameter = new Parameter();
    List<Parameter> parameters = new ArrayList<>(List.of(parameter));

    setField(error, "parameters", parameters);
    assertEquals(parameters, error.getParameters());
  }

  @Test
  void setParametersWorksTest() {
    Parameter parameter = new Parameter();
    List<Parameter> parameters = new ArrayList<>(List.of(parameter));

    setField(error, "parameters", null);

    error.setParameters(parameters);
    assertEquals(parameters, getField(error, "parameters"));
  }

  @Test
  void withParametersWorksTest() {
    Parameter parameter = new Parameter();
    List<Parameter> parameters = new ArrayList<>(List.of(parameter));

    setField(error, "parameters", null);

    Error newError = error.withParameters(parameters);
    assertEquals(parameters, getField(error, "parameters"));
    assertEquals(parameters, getField(newError, "parameters"));
  }

  @Test
  void getAdditionalPropertyWorksTest() {
    Map<String, Object> prop = new HashMap<>(Map.of(KEY, VALUE));

    setField(error, "additionalProperties", prop);
    assertEquals(prop, error.getAdditionalProperties());
    assertEquals(prop.get(KEY), error.getAdditionalProperties().get(KEY));
  }

  @Test
  void setAdditionalPropertyWorksTest() {
    Map<String, Object> prop = new HashMap<>();

    setField(error, "additionalProperties", prop);

    error.setAdditionalProperty(KEY, VALUE);

    @SuppressWarnings("unchecked")
    Map<String, Object> got = (Map<String, Object>) getField(error, "additionalProperties");

    assertEquals(prop, got);
    assertEquals(prop.get(KEY), got.get(KEY));
  }

  @SuppressWarnings("unchecked")
  @Test
  void withAdditionalPropertyWorksTest() {
    Map<String, Object> prop = new HashMap<>();

    setField(error, "additionalProperties", prop);

    Error newError = error.withAdditionalProperty(KEY, VALUE);

    Map<String, Object> got = (Map<String, Object>) getField(error, "additionalProperties");
    assertEquals(prop, got);
    assertEquals(prop.get(KEY), got.get(KEY));

    got = (Map<String, Object>) getField(newError, "additionalProperties");
    assertEquals(prop, got);
    assertEquals(prop.get(KEY), got.get(KEY));
  }

}
