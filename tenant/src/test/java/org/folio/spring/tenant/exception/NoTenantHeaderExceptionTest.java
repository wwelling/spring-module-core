package org.folio.spring.tenant.exception;

import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NoTenantHeaderExceptionTest {

  @Test
  void noTenantHeaderExceptionWorksTest() throws IOException {
    NoTenantHeaderException exception = Assertions.assertThrows(NoTenantHeaderException.class, () -> {
      throw new NoTenantHeaderException(VALUE);
    });

    assertNotNull(exception);
    assertEquals(VALUE, exception.getMessage());
  }
}
