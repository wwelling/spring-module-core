package org.folio.spring.domain.controller;

import java.io.IOException;
import java.util.Optional;
import org.folio.spring.domain.controller.exception.SchemaIOException;
import org.folio.spring.domain.service.JsonSchemasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_/jsonSchemas")
public class JsonSchemasController {

  private static final String CONTENT_TYPE_HEADER = "Content-Type";
  private static final String APPLICATION_SCHEMA_JSON = "application/schema+json";
  private static final String APPLICATION_JSON = "application/json";

  @Autowired
  private JsonSchemasService jsonSchemasService;

  @GetMapping
  public ResponseEntity<Object> getSchemas(
  // @formatter:off
    @RequestParam(value = "path", required = false) Optional<String> path,
    @RequestHeader(value = "x-okapi-url", required = true) String okapiUrl
  // @formatter:on
  ) {
    try {
      if (path.isPresent()) {
        String schema = jsonSchemasService.getSchemaByPath(path.get(), okapiUrl);
        return ResponseEntity.ok().header(CONTENT_TYPE_HEADER, APPLICATION_SCHEMA_JSON).body(schema);
      } else {
        return ResponseEntity.ok().header(CONTENT_TYPE_HEADER, APPLICATION_JSON).body(jsonSchemasService.getSchemas());
      }
    } catch (IOException e) {
      throw new SchemaIOException("Unable to get JSON Schemas!", e);
    }
  }

}
