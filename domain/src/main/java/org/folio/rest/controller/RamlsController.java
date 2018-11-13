package org.folio.rest.controller;

import java.io.IOException;
import java.util.Optional;

import org.folio.rest.controller.exception.SchemaIOException;
import org.folio.rest.service.RamlsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/_/ramls")
public class RamlsController {

  @Autowired
  private RamlsService ramlsService;

  @GetMapping
  public Object getRamls(
  // @formatter:off
    @RequestParam(value = "path", required = false) Optional<String> path,
    @RequestHeader(value = "x-okapi-url", required = true) String okapiUrl
  // @formatter:on    
  ) throws SchemaIOException {
    try {
      if (path.isPresent()) {
        try {
          return ramlsService.getRamlByPath(path.get(), okapiUrl);
        } catch (IOException e) {
          throw new SchemaIOException(String.format("Unable to get RAML %s!", path), e);
        }

      } else {
        return ramlsService.getRamls();
      }
    } catch (IOException e) {
      throw new SchemaIOException("Unable to get RAMLs!", e);
    }
  }

}
