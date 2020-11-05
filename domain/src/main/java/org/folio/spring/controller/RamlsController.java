package org.folio.spring.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.folio.spring.controller.exception.SchemaIOException;
import org.folio.spring.service.RamlsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_/ramls")
public class RamlsController {

  private static final String CONTENT_TYPE_HEADER = "Content-Type";
  private static final String APPLICATION_RAML_YAML = "application/raml+yaml";
  private static final String APPLICATION_JSON = "application/json";

  @Autowired
  private RamlsService ramlsService;

  @GetMapping
  public ResponseEntity<Object> getRamls(
  // @formatter:off
    HttpServletResponse response,
    @RequestParam(value = "path", required = false) Optional<String> path,
    @RequestHeader(value = "x-okapi-url", required = true) String okapiUrl
  // @formatter:on
  ) {
    try {
      if (path.isPresent()) {
        String raml = ramlsService.getRamlByPath(path.get(), okapiUrl);
        return ResponseEntity.ok().header(CONTENT_TYPE_HEADER, APPLICATION_RAML_YAML).body(raml);
      } else {
        return ResponseEntity.ok().header(CONTENT_TYPE_HEADER, APPLICATION_JSON).body(ramlsService.getRamls());
      }
    } catch (IOException e) {
      throw new SchemaIOException("Unable to get RAMLs!", e);
    }
  }

}
