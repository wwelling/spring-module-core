package org.folio.spring.domain.controller.exception;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SchemaNotFoundExceptionTest {

  @Mock
  Throwable throwable;

  @Test
  void schemaNotFoundExceptionWorksTest() throws IOException {
    SchemaNotFoundException exception = Assertions.assertThrows(SchemaNotFoundException.class, () -> {
      throw new SchemaNotFoundException(VALUE);
    });

    assertNotNull(exception);
    assertEquals(VALUE, exception.getMessage());
  }

  @Test
  void schemaNotFoundExceptionWorksWithCauseTest() throws IOException {
    SchemaNotFoundException exception = Assertions.assertThrows(SchemaNotFoundException.class, () -> {
      throw new SchemaNotFoundException(VALUE, throwable);
    });

    assertNotNull(exception);
    assertEquals(VALUE, exception.getMessage());
    assertEquals(throwable, exception.getCause());
  }
}
