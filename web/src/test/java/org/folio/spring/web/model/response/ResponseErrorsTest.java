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
class ResponseErrorsTest {

  @InjectMocks
  private ResponseErrors responseErrors;

  @Test
  void getErrorsWorksTest() {
    Error error = new Error();
    List<Error> errors = new ArrayList<>(List.of(error));

    setField(responseErrors, "errors", errors);
    assertEquals(errors, responseErrors.getErrors());
  }

  @Test
  void setErrorsWorksTest() {
    Error error = new Error();
    List<Error> errors = new ArrayList<>(List.of(error));

    setField(responseErrors, "errors", null);

    responseErrors.setErrors(errors);
    assertEquals(errors, getField(responseErrors, "errors"));
  }

  @Test
  void withErrorsWorksTest() {
    Error error = new Error();
    List<Error> errors = new ArrayList<>(List.of(error));

    setField(responseErrors, "errors", errors);

    ResponseErrors newResp = responseErrors.withErrors(errors);
    assertEquals(errors, getField(responseErrors, "errors"));
    assertEquals(errors, getField(newResp, "errors"));
  }

  @Test
  void gettTotalRecordsWorksTest() {
    Integer totalRecords = 1;

    setField(responseErrors, "totalRecords", totalRecords);
    assertEquals(totalRecords, responseErrors.getTotalRecords());
  }

  @Test
  void settTotalRecordsWorksTest() {
    Integer totalRecords = 1;

    setField(responseErrors, "totalRecords", 0);

    responseErrors.setTotalRecords(totalRecords);
    assertEquals(totalRecords, getField(responseErrors, "totalRecords"));
  }

  @Test
  void withtTotalRecordsWorksTest() {
    Integer totalRecords = 1;

    setField(responseErrors, "totalRecords", 0);

    ResponseErrors newResp = responseErrors.withTotalRecords(totalRecords);
    assertEquals(totalRecords, getField(responseErrors, "totalRecords"));
    assertEquals(totalRecords, getField(newResp, "totalRecords"));
  }

  @Test
  void getAdditionalPropertyWorksTest() {
    Map<String, Object> prop = new HashMap<>(Map.of(KEY, VALUE));

    setField(responseErrors, "additionalProperties", prop);
    assertEquals(prop, responseErrors.getAdditionalProperties());
    assertEquals(prop.get(KEY), responseErrors.getAdditionalProperties().get(KEY));
  }

  @Test
  void setAdditionalPropertyWorksTest() {
    Map<String, Object> prop = new HashMap<>();

    setField(responseErrors, "additionalProperties", prop);

    responseErrors.setAdditionalProperty(KEY, VALUE);

    @SuppressWarnings("unchecked")
    Map<String, Object> got = (Map<String, Object>) getField(responseErrors, "additionalProperties");

    assertEquals(prop, got);
    assertEquals(prop.get(KEY), got.get(KEY));
  }

  @SuppressWarnings("unchecked")
  @Test
  void withAdditionalPropertyWorksTest() {
    Map<String, Object> prop = new HashMap<>();

    setField(responseErrors, "additionalProperties", prop);

    ResponseErrors newResp = responseErrors.withAdditionalProperty(KEY, VALUE);

    Map<String, Object> got = (Map<String, Object>) getField(responseErrors, "additionalProperties");
    assertEquals(prop, got);
    assertEquals(prop.get(KEY), got.get(KEY));

    got = (Map<String, Object>) getField(newResp, "additionalProperties");
    assertEquals(prop, got);
    assertEquals(prop.get(KEY), got.get(KEY));
  }

}
