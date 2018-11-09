package org.folio.rest.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.folio.rest.controller.exception.SchemaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class RamlService {

  @Autowired
  private ResourcePatternResolver resolver;

  public List<String> getRamls() throws IOException {
    List<String> ramls = new ArrayList<>();
    Resource[] resources = resolver.getResources("classpath:ramls/*.raml");
    for (Resource resource : resources) {
      String name = resource.getFilename();
      ramls.add(name);
    }
    return ramls;
  }

  public String getRamlByName(@PathVariable String name) throws IOException {
    Resource resource = resolver.getResource("classpath:ramls/" + name);
    if (resource.exists()) {
      if (resource.isFile() && resource.getFilename().endsWith(".raml")) {
        return IOUtils.toString(resource.getInputStream(), "UTF-8");
      }
    }
    throw new SchemaNotFoundException("RAML " + name + " not found");
  }

}
