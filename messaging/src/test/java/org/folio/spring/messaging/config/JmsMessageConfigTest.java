package org.folio.spring.messaging.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.getField;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { JmsMessageConfig.class })
class JmsMessageConfigTest {

  @InjectMocks
  private JmsMessageConfig jmsMessageConfig;

  @Mock
  private DefaultJmsListenerContainerFactoryConfigurer configurer;

  @Test
  void myFactoryWorksTest() {
    doNothing().when(configurer).configure(any(DefaultJmsListenerContainerFactory.class), any());

    assertNotNull(jmsMessageConfig.myFactory(null, configurer));
    verify(configurer, times(1)).configure(any(DefaultJmsListenerContainerFactory.class), any());
  }

  @Test
  void jacksonJmsMessageConverterWorksTest() {
    MappingJackson2MessageConverter converter = (MappingJackson2MessageConverter) jmsMessageConfig.jacksonJmsMessageConverter();

    assertNotNull(converter);
    assertEquals(MessageType.TEXT, getField(converter, "targetType"));
    assertEquals("_type", getField(converter, "typeIdPropertyName"));
  }
}
