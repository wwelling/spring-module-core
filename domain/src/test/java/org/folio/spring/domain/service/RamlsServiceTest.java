package org.folio.spring.domain.service;

import static org.folio.spring.test.mock.MockMvcConstant.PATH;
import static org.folio.spring.test.mock.MockMvcConstant.URL;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.folio.spring.domain.controller.exception.SchemaNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class RamlsServiceTest {

  private static final String RAMLS = "ramls/workflow";

  private static final String RAMLS_RESPONSE = "!include http://localhost/_/jsonSchemas?path=workflow";

  private static final String RAML = "workflow.raml";

  private static final String RAMLS_RAML = "ramls/workflow.raml";

  private static final String RAMLS_RAML_RESPONSE = "!include http://localhost/_/ramls?path=workflow.raml";

  private static final String RAML_RESPONSE = "!include http://localhost/_/ramls?path=workflow.raml";

  private static final String INCLUDE_OTHER = "!include other";

  private static final String INCLUDE_RAMLS = "!include " + RAMLS;

  private static final String INCLUDE_RAMLS_RAML = "!include " + RAMLS_RAML;

  private static final String INCLUDE_RAML = "!include " + RAML;

  private static final String OTHER_RESPONSE = "!include http://localhost/_/jsonSchemas?path=other";

  @Mock
  private ResourcePatternResolver resolver;

  @Mock
  private Resource resource;

  @InjectMocks
  private RamlsService ramlsService;

  @BeforeEach
  void beforeEach() {
    ramlsService = new RamlsService(resolver);
  }

  @Test
  void getRamlsWorksTest() throws IOException {
    Resource[] resources = { resource };
    ramlsService = new RamlsService(resolver);

    when(resource.getFilename()).thenReturn(VALUE);
    when(resolver.getResources(anyString())).thenReturn(resources);

    List<String> response = ramlsService.getRamls();
    assertTrue(response.contains(VALUE));
  }

  @ParameterizedTest
  @MethodSource("provideGetRamlsByPath")
  void getRamlsByPathWorksTest(String path, String raml, String expect) throws IOException {
    InputStream stream = IOUtils.toInputStream(raml, "UTF-8");

    when(resource.exists()).thenReturn(true);
    when(resource.getInputStream()).thenReturn(stream);
    when(resolver.getResource(anyString())).thenReturn(resource);

    String response = ramlsService.getRamlByPath(path, URL);
    assertEquals(expect, response);
  }

  @Test
  void getRamlsByPathThrowsSchemaNotFoundExceptionTest() throws IOException {
    when(resource.exists()).thenReturn(false);
    when(resolver.getResource(anyString())).thenReturn(resource);

    Assertions.assertThrows(SchemaNotFoundException.class, () -> {
      ramlsService.getRamlByPath(PATH, URL);
    });
  }

  /**
   * Helper function for parameterized test providing tests for the write internal tests.
   *
   * @return
   *   The arguments array stream with the stream columns as:
   *     - String path (the ramls path to use)).
   *     - String raml (the stream value to use)).
   *     - String expect (the returned string to expect)).
   */
  private static Stream<Arguments> provideGetRamlsByPath() {
    return Stream.of(
      Arguments.of(RAMLS,      VALUE,              VALUE),
      Arguments.of(RAMLS_RAML, VALUE,              VALUE),
      Arguments.of(RAML,       VALUE,              VALUE),
      Arguments.of(PATH,       VALUE,              VALUE),
      Arguments.of(RAMLS,      INCLUDE_RAMLS,      RAMLS_RESPONSE),
      Arguments.of(RAMLS_RAML, INCLUDE_RAMLS_RAML, RAMLS_RAML_RESPONSE),
      Arguments.of(RAML,       INCLUDE_RAML,       RAML_RESPONSE),
      Arguments.of(PATH,       INCLUDE_OTHER,      OTHER_RESPONSE)
    );
  }

}
