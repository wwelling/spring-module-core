package org.folio.rest.controller;

import java.io.IOException;
import java.util.List;

import org.folio.rest.controller.exception.SchemaIOException;
import org.folio.rest.service.JsonSchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/_/jsonSchema")
public class JsonSchemaController {

  @Autowired
  private JsonSchemaService jsonSchemaService;

  @GetMapping
  public List<String> getSchemas() throws SchemaIOException {
    try {
      return jsonSchemaService.getSchemas();
    } catch (IOException e) {
      throw new SchemaIOException("Unable to get JSON Schemas!", e);
    }
  }

  @GetMapping("/{name}")
  public JsonNode getSchemaByName(@PathVariable String name, @RequestHeader("x-okapi-url") String okapiUrl) throws SchemaIOException {
    try {
      return jsonSchemaService.getSchemaByName(name, okapiUrl);
    } catch (IOException e) {
      throw new SchemaIOException(String.format("Unable to get JSON Schema %s!", name), e);
    }
  }

}
