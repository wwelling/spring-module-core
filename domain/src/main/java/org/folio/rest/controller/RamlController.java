package org.folio.rest.controller;

import java.io.IOException;
import java.util.List;

import org.folio.rest.controller.exception.SchemaIOException;
import org.folio.rest.service.RamlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_/raml")
public class RamlController {

  @Autowired
  private RamlService ramlService;

  @GetMapping
  public List<String> getRamls() throws SchemaIOException {
    try {
      return ramlService.getRamls();
    } catch (IOException e) {
      throw new SchemaIOException("Unable to get RAMLs!", e);
    }
  }

  @GetMapping(value = "/{name}", produces = "application/raml+yaml")
  public String getRamlByName(
  // @formatter:off
    @PathVariable String name,
    @RequestHeader(value = "x-okapi-url", required = true) String okapiUrl
  // @formatter:on
  ) throws SchemaIOException {
    try {
      return ramlService.getRamlByName(name, okapiUrl);
    } catch (IOException e) {
      throw new SchemaIOException(String.format("Unable to get RAML %s!", name), e);
    }
  }

}
