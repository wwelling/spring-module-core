package org.folio.spring.domain.model;

import static org.folio.spring.test.mock.MockMvcConstant.KEY;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbstractBaseEntityTest {

  private static class ConcreteEntity extends AbstractBaseEntity { } ;

  @InjectMocks
  private ConcreteEntity concreteEntity;

  @Test
  void getIdWorksTest() {
    setField(concreteEntity, "id", VALUE);
    assertEquals(VALUE, concreteEntity.getId());
  }

  @Test
  void setIdWorksTest() {
    setField(concreteEntity, "id", KEY);

    concreteEntity.setId(VALUE);
    assertEquals(VALUE, getField(concreteEntity, "id"));
  }

}
