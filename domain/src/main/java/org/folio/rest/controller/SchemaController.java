package org.folio.rest.controller;

import java.io.IOException;
import java.util.Map;

import org.folio.rest.controller.exception.SchemaIOException;
import org.folio.rest.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/_/schemas")
public class SchemaController {

  @Autowired
  private SchemaService schemaService;

  @GetMapping
  public Map<String, JsonNode> getSchemas() throws SchemaIOException {
    try {
      return schemaService.getSchemas();
    } catch (IOException e) {
      throw new SchemaIOException("Unable to get schemas!", e);
    }
  }

  @GetMapping("/{name}")
  public JsonNode getSchemaByName(@PathVariable String name) throws SchemaIOException {
    try {
      return schemaService.getSchemaByName(name);
    } catch (IOException e) {
      throw new SchemaIOException(String.format("Unable to get schema %s!", name), e);
    }
  }

}
