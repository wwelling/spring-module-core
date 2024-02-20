package org.folio.spring.domain.service;

import static org.folio.spring.test.mock.MockMvcConstant.PATH;
import static org.folio.spring.test.mock.MockMvcConstant.URL;
import static org.folio.spring.test.mock.MockMvcConstant.VALUE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.folio.spring.domain.controller.exception.SchemaNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class RamlsServiceTest {

  private static final String RAMLS_PATH = "ramls/";

  private static final String RAML_EXT = ".raml";

  @Mock
  private ResourcePatternResolver resolver;

  @Mock
  private Resource resource;

  @InjectMocks
  private RamlsService ramlsService;

  @Test
  void getRamlsWorksTest() throws IOException {
    Resource[] resources = { resource };
    ramlsService = new RamlsService(resolver);

    when(resource.getFilename()).thenReturn(VALUE);
    when(resolver.getResources(anyString())).thenReturn(resources);

    List<String> response = ramlsService.getRamls();
    assertTrue(response.contains(VALUE));
  }

  @Test
  void getRamlsByPathWorksWithRamlsPathTest() throws IOException {
    InputStream stream = IOUtils.toInputStream(VALUE, "UTF-8");
    ramlsService = new RamlsService(resolver);

    when(resource.exists()).thenReturn(true);
    when(resource.getInputStream()).thenReturn(stream);
    when(resolver.getResource(anyString())).thenReturn(resource);

    String response = ramlsService.getRamlByPath(RAMLS_PATH, URL);
    assertTrue(response.contains(VALUE));
  }

  @Test
  void getRamlsByPathWorksWithRamlsExtPathTest() throws IOException {
    InputStream stream = IOUtils.toInputStream(VALUE, "UTF-8");
    ramlsService = new RamlsService(resolver);

    when(resource.exists()).thenReturn(true);
    when(resource.getInputStream()).thenReturn(stream);
    when(resolver.getResource(anyString())).thenReturn(resource);

    String response = ramlsService.getRamlByPath(RAML_EXT, URL);
    assertTrue(response.contains(VALUE));
  }

  @Test
  void getRamlsByPathThrowsSchemaNotFoundExceptionTest() throws IOException {
    ramlsService = new RamlsService(resolver);

    when(resource.exists()).thenReturn(false);
    when(resolver.getResource(anyString())).thenReturn(resource);

    Assertions.assertThrows(SchemaNotFoundException.class, () -> {
      ramlsService.getRamlByPath(PATH, URL);
    });
  }

}
