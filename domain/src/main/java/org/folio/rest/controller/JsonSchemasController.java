package org.folio.rest.controller;

import java.io.IOException;
import java.util.Optional;

import org.folio.rest.controller.exception.SchemaIOException;
import org.folio.rest.service.JsonSchemasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_/jsonSchemas")
public class JsonSchemasController {

  @Autowired
  private JsonSchemasService jsonSchemasService;

  @GetMapping
  public Object getSchemas(
  // @formatter:off
    @RequestParam(value = "path", required = false) Optional<String> path,
    @RequestHeader(value = "x-okapi-url", required = true) String okapiUrl
  // @formatter:on    
  ) throws SchemaIOException {
    try {
      if (path.isPresent()) {
        try {
          return jsonSchemasService.getSchemaByPath(path.get(), okapiUrl);
        } catch (IOException e) {
          throw new SchemaIOException(String.format("Unable to get JSON Schema %s!", path), e);
        }
      } else {
        return jsonSchemasService.getSchemas();
      }
    } catch (IOException e) {
      throw new SchemaIOException("Unable to get JSON Schemas!", e);
    }
  }

}
