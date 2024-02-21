package org.folio.spring.web.model.response;

import static org.folio.spring.test.mock.MockMvcConstant.KEY;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParameterTest {

  @InjectMocks
  private Parameter parameter;

  @Test
  void getKeyWorksTest() {
    setField(parameter, "key", KEY);
    assertEquals(KEY, parameter.getKey());
  }

  @Test
  void setKeyWorksTest() {
    setField(parameter, "key", VALUE);

    parameter.setKey(KEY);
    assertEquals(KEY, getField(parameter, "key"));
  }

  @Test
  void withKeyWorksTest() {
    setField(parameter, "key", VALUE);

    Parameter newParam = parameter.withKey(KEY);
    assertEquals(KEY, getField(parameter, "key"));
    assertEquals(KEY, getField(newParam, "key"));
  }

  @Test
  void getValueWorksTest() {
    setField(parameter, "value", VALUE);
    assertEquals(VALUE, parameter.getValue());
  }

  @Test
  void setValueWorksTest() {
    setField(parameter, "value", KEY);

    parameter.setValue(VALUE);
    assertEquals(VALUE, getField(parameter, "value"));
  }

  @Test
  void withValueWorksTest() {
    setField(parameter, "value", KEY);

    Parameter newParam = parameter.withValue(VALUE);
    assertEquals(VALUE, getField(parameter, "value"));
    assertEquals(VALUE, getField(newParam, "value"));
  }

  @Test
  void getAdditionalPropertyWorksTest() {
    Map<String, Object> prop = new HashMap<>(Map.of(KEY, VALUE));

    setField(parameter, "additionalProperties", prop);
    assertEquals(prop, parameter.getAdditionalProperties());
    assertEquals(prop.get(KEY), parameter.getAdditionalProperties().get(KEY));
  }

  @Test
  void setAdditionalPropertyWorksTest() {
    Map<String, Object> prop = new HashMap<>();

    setField(parameter, "additionalProperties", prop);

    parameter.setAdditionalProperty(KEY, VALUE);

    @SuppressWarnings("unchecked")
    Map<String, Object> got = (Map<String, Object>) getField(parameter, "additionalProperties");

    assertEquals(prop, got);
    assertEquals(prop.get(KEY), got.get(KEY));
  }

  @SuppressWarnings("unchecked")
  @Test
  void withAdditionalPropertyWorksTest() {
    Map<String, Object> prop = new HashMap<>();

    setField(parameter, "additionalProperties", prop);

    Parameter newParam = parameter.withAdditionalProperty(KEY, VALUE);

    Map<String, Object> got = (Map<String, Object>) getField(parameter, "additionalProperties");
    assertEquals(prop, got);
    assertEquals(prop.get(KEY), got.get(KEY));

    got = (Map<String, Object>) getField(newParam, "additionalProperties");
    assertEquals(prop, got);
    assertEquals(prop.get(KEY), got.get(KEY));
  }

}
